package com.employee_management.model;

public enum Role {
    MANAGER,
    SUPERVISOR,
    TEAM_LEAD,
    DEVELOPER,
    DESIGNER,
    SALES_REPRESENTATIVE,
    CUSTOMER_SUPPORT,
    HR_SPECIALIST,
    ACCOUNTANT,
    MARKETING_SPECIALIST,
    OPERATIONS_MANAGER,
    RESEARCH_ANALYST,
    CEO,
    ADMIN,
    CTO;

    public String getRole() {
        switch (this) {
            case MANAGER:
                return "Manager";
            case SUPERVISOR:
                return "Supervisor";
            case TEAM_LEAD:
                return "Team Lead";
            case DEVELOPER:
                return "Developer";
            case DESIGNER:
                return "Designer";
            case SALES_REPRESENTATIVE:
                return "Sales Representative";
            case CUSTOMER_SUPPORT:
                return "Customer Support";
            case HR_SPECIALIST:
                return "HR Specialist";
            case ACCOUNTANT:
                return "Accountant";
            case MARKETING_SPECIALIST:
                return "Marketing Specialist";
            case OPERATIONS_MANAGER:
                return "Operations Manager";
            case RESEARCH_ANALYST:
                return "Research Analyst";
            case CEO:
                return "CEO";
            case CTO:
                return "CTO";

        }
        return null;
    }
}
