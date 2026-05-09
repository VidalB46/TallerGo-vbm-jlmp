/**
 * TALLER GO - SCRIPTS GLOBALES
 */
document.addEventListener("DOMContentLoaded", function() {

    const fileTooLargeMessage = document.body.dataset.fileTooLarge || "El archivo es demasiado grande. El límite máximo es de 50MB.";

    // 1. VALIDACIÓN GENÉRICA DE FORMULARIOS
    const forms = document.querySelectorAll(".needs-validation");
    Array.from(forms).forEach(form => {
        form.addEventListener("submit", event => {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();

                const firstInvalidField = form.querySelector(":invalid");
                if (firstInvalidField) {
                    firstInvalidField.scrollIntoView({ behavior: "smooth", block: "center" });
                    setTimeout(() => firstInvalidField.focus(), 300);
                }
            }
            form.classList.add("was-validated");
        }, false);
    });

    // 2. VALIDACIÓN DE PESO DE ARCHIVOS
    const fileInputs = document.querySelectorAll('input[type="file"]');
    fileInputs.forEach(input => {
        input.addEventListener("change", function() {
            const maxSize = 50 * 1024 * 1024;
            if (this.files && this.files[0]) {
                if (this.files[0].size > maxSize) {
                    alert(fileTooLargeMessage);
                    this.value = "";
                }
            }
        });
    });

    // 3. AUTO-OCULTAR ALERTAS DE ÉXITO
    const successAlerts = document.querySelectorAll(".alert-success");
    successAlerts.forEach(alert => {
        setTimeout(() => {
            alert.style.transition = "opacity 0.5s ease";
            alert.style.opacity = "0";
            setTimeout(() => alert.remove(), 500);
        }, 5000);
    });

    // 4. DROPDOWN DE USUARIO Y PERSONAL
    document.querySelectorAll(".tgo-avatar-wrap, #ddPersonal").forEach(function(wrap) {
        const btn = wrap.querySelector(".tgo-avatar-btn, .tgo-dd-btn");
        if (!btn) return;
        btn.addEventListener("click", function(e) {
            e.stopPropagation();
            const isOpen = wrap.classList.contains("open");
            document.querySelectorAll(".tgo-avatar-wrap.open, #ddPersonal.open")
                .forEach(function(el) { el.classList.remove("open"); });
            if (!isOpen) wrap.classList.add("open");
        });
    });

    document.addEventListener("click", function() {
        document.querySelectorAll(".tgo-avatar-wrap.open, #ddPersonal.open")
            .forEach(function(el) { el.classList.remove("open"); });
    });

    document.querySelectorAll(".tgo-dd-menu").forEach(function(menu) {
        menu.addEventListener("click", function(e) { e.stopPropagation(); });
    });
});