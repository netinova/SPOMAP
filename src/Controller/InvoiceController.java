package Controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import Model.AppState;
import Model.Invoice;
import Model.UserType;
import Service.InvoiceService;
import Util.Validator;
import View.InvoiceView;

public class InvoiceController {

    @SuppressWarnings("unused")
    private OnChangeViewListener listener;
    private InvoiceView view;
    private InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    public void setOnChangeViewListener(OnChangeViewListener listener) {
        this.listener = listener;
    }

    public void setView(InvoiceView view) {
        this.view = view;
    }

    public Validator.ValidationResult validationDate(String date) {
        return Validator.validationManiDate(date);
    }

    public void handleSearch() {
        if (view == null || invoiceService == null)
            return;

        String invoiceId = view.getSearchInvoiceId();
        String userId = view.getSearchUserId();
        String dateFromStr = view.getSearchDateFrom();
        String dateToStr = view.getSearchDateTo();

        var loggedUser = AppState.getInstance().getLoggedInUser();
        if (loggedUser == null)
            return;

        if (loggedUser.getUserType() != UserType.ADMIN) {
            userId = loggedUser.getUserId();
        } else if (userId == null || userId.isEmpty()) {
            userId = null;
        }

        if (invoiceId == null || invoiceId.isEmpty())
            invoiceId = null;
        if (userId == null || userId.isEmpty())
            userId = null;

        LocalDateTime dateFrom = parseDate(dateFromStr);
        LocalDateTime dateTo = parseDateEndOfDay(dateToStr);

        List<Invoice> results = invoiceService.searchInvoices(invoiceId, userId, dateFrom, dateTo);
        view.displayInvoices(results);
    }

    private LocalDateTime parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty())
            return null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDate date = LocalDate.parse(dateStr, formatter);
            return date.atStartOfDay();
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private LocalDateTime parseDateEndOfDay(String dateStr) {
        if (dateStr == null || dateStr.isEmpty())
            return null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDate date = LocalDate.parse(dateStr, formatter);
            return date.atTime(LocalTime.MAX);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public void onInvoiceCardClick(Invoice invoice) {

    }
}
