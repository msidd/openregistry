/**
 * Copyright (C) 2009 Jasig, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openregistry.core.web;

import org.openregistry.core.service.PersonService;
import org.springframework.binding.message.MessageContext;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.openregistry.core.domain.sor.SorRole;
import org.openregistry.core.domain.sor.SorPerson;
import org.openregistry.core.domain.sor.SorSponsor;
import org.openregistry.core.domain.*;
import org.openregistry.core.service.ServiceExecutionResult;
import org.openregistry.core.repository.ReferenceRepository;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Nancy Mond
 * Date: May 4, 2009
 * Time: 10:52:01 AM
 * To change this template use File | Settings | File Templates.
 */
@Component
public final class RoleAction extends AbstractPersonServiceAction {

    private final ReferenceRepository referenceRepository;

    @Autowired(required=true)
    public RoleAction(final PersonService personService, final ReferenceRepository referenceRepository) {
        super(personService);
        this.referenceRepository = referenceRepository;
    }

    protected final String ACTIVE_STATUS = "Active";
    protected final String CAMPUS = "Campus";
    protected final String PERSON = "Person";
    protected final String CELL = "Cell";

    public SorRole initSorRole(final SorPerson sorPerson, final String roleInfoCode) {
        final RoleInfo roleInfo = referenceRepository.getRoleInfoByCode(roleInfoCode);
        return addRole(sorPerson, roleInfo);
    }

     /**
     * Add and initialize new role.
     * @param sorPerson
     * @param roleInfo
     * @return sorRole
     */
    protected SorRole addRole(final SorPerson sorPerson, final RoleInfo roleInfo){
        final SorRole sorRole = sorPerson.addRole(roleInfo);
        sorRole.setSorId("1");  // TODO Don't hardcode OR-56
        sorRole.setSourceSorIdentifier("or-webapp"); // TODO Don't hardcode OR-55
        logger.info("status: "+Type.DataTypes.STATUS.name());
        sorRole.setPersonStatus(referenceRepository.findType(Type.DataTypes.STATUS, ACTIVE_STATUS));
        final EmailAddress emailAddress = sorRole.addEmailAddress();
        emailAddress.setAddressType(referenceRepository.findType(Type.DataTypes.EMAIL, CAMPUS));
        final Phone phone = sorRole.addPhone();
        phone.setPhoneType(referenceRepository.findType(Type.DataTypes.PHONE, CELL));
        phone.setAddressType(referenceRepository.findType(Type.DataTypes.ADDRESS, CAMPUS));
        final Address address = sorRole.addAddress();
        address.setType(referenceRepository.findType(Type.DataTypes.ADDRESS, CAMPUS));
        final SorSponsor sponsor = sorRole.setSponsor();
        sponsor.setType(referenceRepository.findType(Type.DataTypes.SPONSOR, PERSON));  // TODO handle other types OR-57

        //provide default values for start and end date of role
        final Calendar cal = Calendar.getInstance();
        sorRole.setStart(cal.getTime());
        cal.add(Calendar.MONTH, 6);
        sorRole.setEnd(cal.getTime());
        return sorRole;
    }

    public boolean isRoleNewForPerson(SorPerson sorPerson, String roleInfoCode, MessageContext context) {
        //check if person already has the role to be added.
        logger.info("IsRoleNewForPerson: code:"+ roleInfoCode);
        final Person person = getPersonService().findPersonById(sorPerson.getPersonId());
        if (person.pickOutRole(roleInfoCode) != null){
            context.addMessage(new MessageBuilder().error().code("roleAlreadyExists").build());
            return false;
        }
        return true;
    }

    public boolean addSorRole(final SorPerson sorPerson, final SorRole sorRole, final MessageContext context) {
        final ServiceExecutionResult<SorRole> result = getPersonService().validateAndSaveRoleForSorPerson(sorPerson, sorRole);
        return convertAndReturnStatus(result, context, null);
    }

    public boolean updateSorRole(final SorRole role, final MessageContext context) {
        final ServiceExecutionResult<SorRole> result = getPersonService().updateSorRole(role);
        return convertAndReturnStatus(result, context, "roleUpdated");
    }

    public boolean expireRole(final SorRole role, final MessageContext context) {
        boolean result = this.getPersonService().expireRole(role);
        return true;
    }

    public boolean renewRole(final SorRole role, final MessageContext context) {
        boolean result = this.getPersonService().renewRole(role);
        return true;
    }
}
