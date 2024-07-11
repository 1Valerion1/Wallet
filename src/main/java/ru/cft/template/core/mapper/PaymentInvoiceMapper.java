package ru.cft.template.core.mapper;


import org.mapstruct.Mapper;
import ru.cft.template.api.dto.PaymentInvoiceDto;
import ru.cft.template.core.entity.PaymentInvoice;

import java.util.List;

@Mapper(uses = UserMapper.class)
public interface PaymentInvoiceMapper {
    PaymentInvoiceDto map(PaymentInvoice paymentInvoice);

    List<PaymentInvoiceDto> mapList(List<PaymentInvoice> paymentInvoiceList);

}
