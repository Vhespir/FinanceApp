import java.util.Scanner;

public class PaycheckCalculator {
    public static void main(String[] args) {

        final double OVERTIME_MULTIPLIER = 1.5; // Overtime pay multiplier
        final int REGULAR_HOURS = 40; // Standard work hours
        final double MEDICARE_RATE = 0.0145; // Medicare tax rate
        final double SOCIAL_SECURITY_RATE = 0.062; // Social Security tax rate

    // Define tax brackets (weekly income range and rates)
        // Need to adjust rates to reflect allowances claimed. More research needed.
        double[][] taxBrackets = {
                {0, 223.08, 0.04029}, // 10%
                {223.08, 917.31, 0.06}, // 12%
                {917.32, 1971.15, 0.22}, // 22%
                {1971.16, 3519.23, 0.24}, // 24%
                {3519.24, 4401.92, 0.32}, // 32%
                {4401.93, 10628.85, 0.35}, // 35%
                {10628.86, Double.MAX_VALUE, 0.37} // 37%
        };

        Scanner scanner = new Scanner(System.in);

        try {
            // Input: Weekly hours and hourly wage
            System.out.println("How many hours did you work this week? ");
            double weeklyHours = scanner.nextDouble();

            if (weeklyHours < 0) {
                System.out.println("Hours worked cannot be negative.");
                return;
            }

            System.out.println("What is your hourly wage? ");
            double hourlyRate = scanner.nextDouble();

            if (hourlyRate < 0) {
                System.out.println("Hourly wage cannot be negative.");
                return;
            }

            // Input: Pre-tax deductions for insurance
            System.out.println("Enter weekly medical insurance deduction: ");
            double medicalInsurance = scanner.nextDouble();

            System.out.println("Enter weekly vision insurance deduction: ");
            double visionInsurance = scanner.nextDouble();

            System.out.println("Enter weekly dental insurance deduction: ");
            double dentalInsurance = scanner.nextDouble();

            // Calculate regular and overtime pay
            double weeklyRegularPay;
            double weeklyOvertimePay = 0;

            if (weeklyHours > REGULAR_HOURS) {
                double weeklyOvertimeHours = weeklyHours - REGULAR_HOURS;
                weeklyRegularPay = REGULAR_HOURS * hourlyRate;
                weeklyOvertimePay = weeklyOvertimeHours * (hourlyRate * OVERTIME_MULTIPLIER);
            } else {
                weeklyRegularPay = weeklyHours * hourlyRate;
            }

            double weeklyGrossPay = weeklyRegularPay + weeklyOvertimePay;

            // Calculate total pre-tax deductions
            double totalPreTaxDeductions = medicalInsurance + visionInsurance + dentalInsurance;

            // Adjust gross pay for pre-tax deductions
            double taxableIncome = weeklyGrossPay - totalPreTaxDeductions;

            // Calculate Medicare and Social Security taxes
            double medicareTax = taxableIncome * MEDICARE_RATE;
            double socialSecurityTax = taxableIncome * SOCIAL_SECURITY_RATE;

            // Calculate federal income tax using tax brackets
            double federalIncomeTax = calculateIncomeTax(taxableIncome, taxBrackets);

            // Calculate total deductions (taxes + insurance)
            double totalDeductions = totalPreTaxDeductions + medicareTax + socialSecurityTax + federalIncomeTax;

            // Calculate net pay
            double weeklyNetPay = weeklyGrossPay - totalDeductions;

            // Output results
            System.out.println("Regular Pay: $" + String.format("%.2f", weeklyRegularPay));
            System.out.println("Overtime Pay: $" + String.format("%.2f", weeklyOvertimePay));
            System.out.println("Gross Pay: $" + String.format("%.2f", weeklyGrossPay));
            System.out.println("Pre-Tax Deductions (Insurance): $" + String.format("%.2f", totalPreTaxDeductions));
            System.out.println("Taxable Income: $" + String.format("%.2f", taxableIncome));
            System.out.println("Medicare Tax: $" + String.format("%.2f", medicareTax));
            System.out.println("Social Security Tax: $" + String.format("%.2f", socialSecurityTax));
            System.out.println("Federal Income Tax: $" + String.format("%.2f", federalIncomeTax));
            System.out.println("Total Deductions: $" + String.format("%.2f", totalDeductions));
            System.out.println("Net Pay: $" + String.format("%.2f", weeklyNetPay));

        } catch (Exception e) {
            System.out.println("Invalid input. Please enter valid numbers.");
        } finally {
            scanner.close();
        }
    }

    // Method to calculate income tax based on tax brackets
    public static double calculateIncomeTax(double income, double[][] taxBrackets) {
        double tax = 0.0;

        for (double[] bracket : taxBrackets) {
            double lowerBound = bracket[0];
            double upperBound = bracket[1];
            double rate = bracket[2];

            if (income > lowerBound) {
                double taxableAmount = Math.min(income, upperBound) - lowerBound;
                tax += taxableAmount * rate;
            } else {
                break; // No further brackets apply
            }
        }

        return tax;
    }
}
// Implement allowance differences in tax rate
// Implement other deductions
