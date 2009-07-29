package org.openregistry.integration.support;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.openregistry.integration.IdentifierChangeEventNotificationService;
import org.openregistry.core.domain.IdentifierType;
import org.openregistry.core.domain.Identifier;

/**
 * Aspect to intercept identifier change service invocations and fire identifier change event messages
 * to an external integration endpoint
 * @since 1.0
 */
@Aspect
@Component
public class IdentifierChangeAspect {

    @Autowired
    private IdentifierChangeEventNotificationService idChangeNotificationService;

    @AfterReturning("(execution (* org.openregistry.core.service.IdentifierChangeService.change(..)))")
    public void fireIdentifierChangeEvent(final JoinPoint joinPoint) {
        IdentifierType internalType = (IdentifierType)joinPoint.getArgs()[0];
        Identifier internalId = (Identifier)joinPoint.getArgs()[1];
        IdentifierType changedType = (IdentifierType)joinPoint.getArgs()[2];
        Identifier changedId = (Identifier)joinPoint.getArgs()[3];
        this.idChangeNotificationService.createAndSendEventMessageFor(internalType.getName(), internalId.getValue(), changedType.getName(), changedId.getValue());                
    }
}