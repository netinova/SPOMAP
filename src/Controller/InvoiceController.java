package Controller;

import Util.Validator;
import View.InvoiceView;

public class InvoiceController {

    @SuppressWarnings("unused")
    private OnChangeViewListener listener;

    public void setOnChangeViewListener(OnChangeViewListener listener) {
        this.listener = listener;
    }

    @SuppressWarnings("unused")
    private InvoiceView view;

    public InvoiceController() {

    }

    public void setView(InvoiceView view) {
        this.view = view;
    }

    public Validator.ValidationResult validationDate(String date) {
        return Validator.validationDate(date);
    }

}
