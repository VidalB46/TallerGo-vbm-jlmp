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

                    // Buscar el primer campo con error y hacer scroll suave hacia él
                    const firstInvalidField = form.querySelector(':invalid');
                    if (firstInvalidField) {
                        firstInvalidField.scrollIntoView({ behavior: 'smooth', block: 'center' });
                        // Pequeño retardo para enfocarlo después del scroll
                        setTimeout(() => firstInvalidField.focus(), 300);
                    }
                }
                // Añade la clase que activa los estilos rojos en CSS
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

    // 4. DROPDOWN DE USUARIO Y PERSONAL (solo clic, nunca hover)
    document.querySelectorAll('.tgo-avatar-wrap, #ddPersonal').forEach(function(wrap) {
        const btn = wrap.querySelector('.tgo-avatar-btn, .tgo-dd-btn');
        if (!btn) return;
        btn.addEventListener('click', function(e) {
            e.stopPropagation();
            const isOpen = wrap.classList.contains('open');
            // Cerrar todos los dropdowns abiertos
            document.querySelectorAll('.tgo-avatar-wrap.open, #ddPersonal.open')
                    .forEach(function(el) { el.classList.remove('open'); });
            if (!isOpen) wrap.classList.add('open');
        });
    });
    // Cerrar al hacer clic fuera
    document.addEventListener('click', function() {
        document.querySelectorAll('.tgo-avatar-wrap.open, #ddPersonal.open')
                .forEach(function(el) { el.classList.remove('open'); });
    });
    // Evitar que un clic dentro del menú lo cierre
    document.querySelectorAll('.tgo-dd-menu').forEach(function(menu) {
        menu.addEventListener('click', function(e) { e.stopPropagation(); });
    });
});