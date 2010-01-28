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
package org.openregistry.core.domain.validation;

import org.openregistry.core.domain.sor.SoRSpecification;
import org.openregistry.core.domain.sor.SystemOfRecordHolder;

import javax.validation.ConstraintValidator;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract class that exposes a method for obtaining the SoRSpecification.
 *
 * @version $Revision$ $Date$
 * @since 0.1
 */
public abstract class AbstractSystemOfRecordConstraintValidator<A extends Annotation,T> implements ConstraintValidator<A,T> {

    protected SoRSpecification getSoRSpecification() {
        return SystemOfRecordHolder.getCurrentSystemOfRecord();
    }
}
