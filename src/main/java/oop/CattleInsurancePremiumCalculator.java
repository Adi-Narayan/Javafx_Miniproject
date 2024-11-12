package oop;

import java.util.HashMap;
import java.util.Map;

public class CattleInsurancePremiumCalculator
{
    public static class InsurancePolicy extends Cattle
    {
        protected String policyType;
        protected double basePremiumRate;
        protected double sumInsured;
        private int cattleAge;
        private String cattleBreed;
        private boolean isHealthCertified;
        private String purpose; // dairy/beef/breeding
        private int claimHistory;

        public InsurancePolicy(String username, String password, String email,
                               String breed, String age, String weight, String UID,
                               String policyType, double sumInsured, boolean isHealthCertified,
                               String purpose, int claimHistory)
        {
            super(username, password, email, breed, age, weight, UID, isHealthCertified);
            this.policyType = policyType;
            this.sumInsured = sumInsured;
            this.cattleAge = Integer.parseInt(age);
            this.cattleBreed = breed;
            this.isHealthCertified = isHealthCertified;
            this.purpose = purpose;
            this.claimHistory = claimHistory;
            this.basePremiumRate = getBasePremiumRate(policyType);
        }

        private double getBasePremiumRate(String policyType)
        {
            switch(policyType) {
                case "New India Assurance": return 0.03;
                case "United India Insurance": return 0.045;
                case "Oriental Insurance": return 0.05;
                case "Livestock Insurance Scheme (LIS)": return 0.04;
                case "Integrated Rural Development Program (IRDP) Insurance": return 0.02;
                case "National Livestock Mission": return 0.05;
                case "Rashtriya Gokul Mission insurance provisions": return 0.05;
                case "Dairy Entrepreneurship Development Scheme": return 0.05;
                default: return 0.035;
            }
        }
    }

    public static class PremiumCalculationResult
    {
        double basePremium;
        double adjustedPremium;
        Map<String, Double> adjustmentFactors;
        double finalPremium;
        double gst;
        double totalAmount;

        public PremiumCalculationResult() {
            this.adjustmentFactors = new HashMap<>();
        }
    }

    public PremiumCalculationResult calculatePremium(InsurancePolicy policy) {
        PremiumCalculationResult result = new PremiumCalculationResult();

        result.basePremium = policy.sumInsured * policy.basePremiumRate;

        double ageFactor = calculateAgeFactor(policy.cattleAge);
        result.adjustmentFactors.put("Age", ageFactor);

        double breedFactor = calculateBreedFactor(policy.cattleBreed);
        result.adjustmentFactors.put("Breed", breedFactor);

        double healthFactor = policy.isHealthCertified ? 0.9 : 1.0; // 10% discount if certified
        result.adjustmentFactors.put("Health", healthFactor);

        double purposeFactor = calculatePurposeFactor(policy.purpose);
        result.adjustmentFactors.put("Purpose", purposeFactor);

        double claimFactor = calculateClaimFactor(policy.claimHistory);
        result.adjustmentFactors.put("Claims", claimFactor);

        // Calculate Adjusted Premium
        result.adjustedPremium = result.basePremium;
        for (double factor : result.adjustmentFactors.values()) {
            result.adjustedPremium *= factor;
        }

        // 7. Apply GST (18%)
        result.finalPremium = result.adjustedPremium;
        result.gst = result.finalPremium * 0.18;
        result.totalAmount = result.finalPremium + result.gst;

        return result;
    }

    private double calculateAgeFactor(int age) {
        if (age < 2) return 1.2;      // Higher risk for very young cattle
        if (age <= 5) return 1.0;     // Base rate for prime age
        if (age <= 8) return 1.1;     // Slightly higher for older cattle
        return 1.3;                   // Much higher for very old cattle
    }

    private double calculateBreedFactor(String breed) {
        return switch (breed.toUpperCase()) {
            case "Male" -> 0.9;    // Lower risk
            case "Female" -> 1.0;     // Base rate
            default -> 1.0;
        };
    }

    private double calculatePurposeFactor(String purpose) {
        switch(purpose.toUpperCase()) {
            case "DAIRY": return 1.1;      // Higher risk due to production stress
            case "BEEF": return 1.0;       // Base rate
            case "BREEDING": return 1.15;   // Higher risk due to breeding complications
            default: return 1.0;
        }
    }

    private double calculateClaimFactor(int previousClaims) {
        if (previousClaims == 0) return 0.9;     // No claims bonus
        if (previousClaims == 1) return 1.0;     // Base rate
        if (previousClaims == 2) return 1.2;     // Higher risk
        return 1.4;                              // Much higher risk
    }
}