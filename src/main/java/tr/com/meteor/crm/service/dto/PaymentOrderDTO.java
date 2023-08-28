package tr.com.meteor.crm.service.dto;

import tr.com.meteor.crm.domain.AttributeValue;

import java.time.Instant;
import java.util.UUID;

public class PaymentOrderDTO {
    private UUID paymentId;

    private AttributeValue status;

    private Instant okeyFirst;

    public UUID getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(UUID paymentId) {
        this.paymentId = paymentId;
    }

    public AttributeValue getStatus() {
        return status;
    }

    public void setStatus(AttributeValue status) {
        this.status = status;
    }

    public Instant getOkeyFirst() {
        return okeyFirst;
    }

    public void setOkeyFirst(Instant okeyFirst) {
        this.okeyFirst = okeyFirst;
    }
}
