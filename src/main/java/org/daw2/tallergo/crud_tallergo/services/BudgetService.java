package org.daw2.tallergo.crud_tallergo.services;

import org.daw2.tallergo.crud_tallergo.dtos.BudgetCreateDTO;
import org.daw2.tallergo.crud_tallergo.dtos.BudgetDTO;
import org.daw2.tallergo.crud_tallergo.dtos.BudgetDetailDTO;
import org.daw2.tallergo.crud_tallergo.dtos.BudgetUpdateDTO;

public interface BudgetService {
    BudgetDetailDTO getBudgetById(Long id);
    BudgetDetailDTO getBudgetByRepairId(Long repairId);
    BudgetDTO createBudget(BudgetCreateDTO dto);
    BudgetDTO updateBudget(BudgetUpdateDTO dto);
    void deleteBudget(Long id);
    void rejectBudget(Long id);
}