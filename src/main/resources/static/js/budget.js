document.addEventListener("DOMContentLoaded", function() {
    const container = document.getElementById("linesContainer");
    if (container && container.children.length === 0) {
        addLine();
    }
});

function getBudgetI18n() {
    const el = document.getElementById("budgetI18n");
    return el ? el.dataset : {};
}

function budgetText(key, fallback = "") {
    const texts = getBudgetI18n();
    return texts[key] || fallback;
}

function showErrorModal(message) {
    document.getElementById("errorModalMessage").textContent = message;
    document.getElementById("customErrorModal").classList.add("open");
}

function closeErrorModal() {
    document.getElementById("customErrorModal").classList.remove("open");
}

function addLine() {
    const container = document.getElementById("linesContainer");
    if (!container) return;

    const index = container.querySelectorAll(".line-item").length;

    const lineHtml = `
        <div class="line-item">
            <div>
                <label class="form-label text-muted" style="font-size: 0.75rem;">${budgetText("conceptLabel", "Concepto / Descripción")}</label>
                <input type="text"
                       name="lines[${index}].concept"
                       list="commonParts"
                       class="form-control-custom concept-input"
                       required
                       placeholder="${budgetText("conceptPlaceholder", "Ej: Filtro de aceite")}" />
                <div class="invalid-feedback-custom">${budgetText("requiredText", "Requerido")}</div>
            </div>
            <div>
                <label class="form-label text-muted" style="font-size: 0.75rem;">${budgetText("quantityLabel", "Cantidad")}</label>
                <input type="number"
                       name="lines[${index}].quantity"
                       min="1"
                       value="1"
                       class="form-control-custom qty-input"
                       required />
                <div class="invalid-feedback-custom">${budgetText("minText", "Mínimo 1")}</div>
            </div>
            <div>
                <label class="form-label text-muted" style="font-size: 0.75rem;">${budgetText("priceLabel", "Precio Un. (€)")}</label>
                <input type="number"
                       name="lines[${index}].unitPrice"
                       step="0.01"
                       min="0"
                       class="form-control-custom price-input"
                       required
                       placeholder="0.00" />
                <div class="invalid-feedback-custom">${budgetText("requiredText", "Requerido")}</div>
            </div>
            <button type="button"
                    class="btn-remove-line"
                    onclick="removeLine(this)"
                    title="${budgetText("removeTitle", "Eliminar concepto")}">
                <i class="bi bi-trash3"></i>
            </button>
        </div>
    `;

    container.insertAdjacentHTML("afterbegin", lineHtml);
    reindexLines();
}

function removeLine(buttonElement) {
    const container = document.getElementById("linesContainer");
    if (container.querySelectorAll(".line-item").length > 1) {
        buttonElement.closest(".line-item").remove();
        reindexLines();
    } else {
        showErrorModal(
            budgetText("minLineError", "El presupuesto debe contener al menos un concepto facturable.")
        );
    }
}

function reindexLines() {
    const lines = document.querySelectorAll(".line-item");
    lines.forEach((line, index) => {
        line.querySelector(".concept-input").name = `lines[${index}].concept`;
        line.querySelector(".qty-input").name = `lines[${index}].quantity`;
        line.querySelector(".price-input").name = `lines[${index}].unitPrice`;
    });
}