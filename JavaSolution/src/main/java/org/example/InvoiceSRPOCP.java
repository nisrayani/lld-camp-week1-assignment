package org.example;
// InvoiceSRPOCP.java
// Messy starter: Monolith Invoice Service (violates SRP + OCP)

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class LineItem {
    String sku;
    int quantity;
    double unitPrice;

    LineItem(String sku, int quantity, double unitPrice) {
        this.sku = sku;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }
}


class Invoice {
    List<LineItem> items;
    Map<String, Double> discounts;

    public Invoice(List<LineItem> items, Map<String, Double> discounts) {
        this.items = items;
        this.discounts = discounts;
    }

    public List<LineItem> getItems() {
        return items;
    }

    public void setItems(List<LineItem> items) {
        this.items = items;
    }

    public Map<String, Double> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(Map<String, Double> discounts) {
        this.discounts = discounts;
    }

    public double getSubtotal() {
        // pricing
        double subtotal = 0.0;
        for (LineItem it : items) subtotal += it.unitPrice * it.quantity;
        return subtotal;
    }

    public double getDiscountTotal(double subtotal) {
        // discounts (tightly coupled)
        double discountTotal = 0.0;
        for (Map.Entry<String, Double> e : discounts.entrySet()) {
            String k = e.getKey();
            double v = e.getValue();
            DiscountCalculationStrategy discountCalculationStrategy = DiscountFactory.getDiscountStrategy(k, v);
            discountTotal += discountCalculationStrategy.calculateDiscountTotal(subtotal);
        }
        return discountTotal;
    }
}

abstract class DiscountCalculationStrategy {
    abstract double calculateDiscountTotal(double subtotal);
}

class PercentOffDiscountStrategy extends DiscountCalculationStrategy {
    private final double percentage;

    PercentOffDiscountStrategy(double percentage) {
        this.percentage = percentage;
    }

    @Override
    public double calculateDiscountTotal(double subtotal) {
        return subtotal * (percentage / 100.0);
    }
}

class FlatOffDiscountStrategy extends DiscountCalculationStrategy {

    private double flatoff;

    FlatOffDiscountStrategy(double flatoff) {
        this.flatoff = this.flatoff;
    }

    @Override
    public double calculateDiscountTotal(double subtotal) {
        return flatoff;
    }
}

class DiscountFactory {
    public static DiscountCalculationStrategy getDiscountStrategy(String type, double discountValue) {
        return switch (type) {
            case ("percent_off") -> new PercentOffDiscountStrategy(discountValue);
            case ("flat_off") -> new FlatOffDiscountStrategy(discountValue);
            default -> throw new IllegalArgumentException("No strategy found");
        };
    }
}

class InvoiceProcessor {
    private Invoice invoice;

    public InvoiceProcessor(Invoice invoice) {
        this.invoice = invoice;
    }

    String process(String email) {

        double subtotal = invoice.getSubtotal();
        double discountTotal = invoice.getDiscountTotal(subtotal);


        // tax inline
        double tax = (subtotal - discountTotal) * 0.18;
        double grand = subtotal - discountTotal + tax;

        // rendering inline (pretend PDF)
        StringBuilder pdf = new StringBuilder();
        pdf.append("INVOICE\n");
        for (LineItem it : invoice.getItems()) {
            pdf.append(it.sku).append(" x").append(it.quantity).append(" @ ").append(it.unitPrice).append("\n");
        }
        pdf.append("Subtotal: ").append(subtotal).append("\n")
                .append("Discounts: ").append(discountTotal).append("\n")
                .append("Tax: ").append(tax).append("\n")
                .append("Total: ").append(grand).append("\n");

        // email I/O inline (tight coupling)
        if (email != null && !email.isEmpty()) {
            System.out.println("[SMTP] Sending invoice to " + email + "...");
        }

        // logging inline
        System.out.println("[LOG] Invoice processed for " + email + " total=" + grand);

        return pdf.toString();
    }
}


class InvoiceProcessorTester {
    double computeTotal(Invoice invoice) {
        InvoiceProcessor invoiceProcessor = new InvoiceProcessor(invoice);
        String rendered = invoiceProcessor.process("noreply@example.com");
        int idx = rendered.lastIndexOf("Total:");
        if (idx < 0) throw new RuntimeException("No total");
        String num = rendered.substring(idx + 6).trim();
        return Double.parseDouble(num);
    }
}

class InvoiceService {
    // TO FIX (SRP): Does pricing, discounting, tax, rendering, email, logging.
    // TO FIX (OCP): Discount types hard-coded with if/else.


}

public class InvoiceSRPOCP {
    public static void main(String[] args) {
        List<LineItem> items = Arrays.asList(
                new LineItem("BOOK-001", 2, 500.0),
                new LineItem("USB-DRIVE", 1, 799.0)
        );
        Map<String, Double> discounts = new HashMap<>();
        discounts.put("percent_off", 10.0);
        Invoice invoice = new Invoice(items, discounts);
        InvoiceProcessor processor = new InvoiceProcessor(invoice);
        System.out.println(processor.process("customer@example.com"));
    }
}
