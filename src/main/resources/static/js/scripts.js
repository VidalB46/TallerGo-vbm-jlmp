/**
 * TALLER GO - SCRIPTS GLOBALES
 */
document.addEventListener("DOMContentLoaded", function() {

    // 1. VALIDACIÓN GENÉRICA DE FORMULARIOS
    const forms = document.querySelectorAll('.needs-validation');
    Array.from(forms).forEach(form => {
        form.addEventListener('submit', event => {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        }, false);
    });

    // 2. VALIDACIÓN DE PESO DE ARCHIVOS
    const fileInputs = document.querySelectorAll('input[type="file"]');
    fileInputs.forEach(input => {
        input.addEventListener('change', function() {
            const maxSize = 50 * 1024 * 1024;
            if (this.files && this.files[0]) {
                if (this.files[0].size > maxSize) {
                    alert("El archivo es demasiado grande. El límite máximo es de 50MB.");
                    this.value = "";
                }
            }
        });
    });

    // 3. AUTO-OCULTAR ALERTAS DE ÉXITO
    const successAlerts = document.querySelectorAll('.alert-success');
    successAlerts.forEach(alert => {
        setTimeout(() => {
            alert.style.transition = "opacity 0.5s ease";
            alert.style.opacity = "0";
            setTimeout(() => alert.remove(), 500);
        }, 5000);
    });
});