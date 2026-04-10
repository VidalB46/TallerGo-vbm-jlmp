/**
 * TALLER GO - SCRIPTS GLOBALES
 * Validación Frontend y utilidades de usabilidad.
 */

document.addEventListener("DOMContentLoaded", function() {

    // ========================================================================
    // 1. VALIDACIÓN GENÉRICA DE FORMULARIOS (Estilo Bootstrap)
    // Evita que el formulario se envíe si faltan campos obligatorios HTML5.
    // ========================================================================
    const forms = document.querySelectorAll('.needs-validation');
    Array.from(forms).forEach(form => {
        form.addEventListener('submit', event => {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
                // Opcional: Mostrar un alert genérico
                // alert("Por favor, completa correctamente todos los campos obligatorios.");
            }
            form.classList.add('was-validated');
        }, false);
    });

    // ========================================================================
    // 2. VALIDACIÓN DE PESO DE ARCHIVOS (Subida de fotos/vídeos de averías)
    // Previene que el cliente intente subir un archivo mayor de 50MB.
    // ========================================================================
    const fileInputs = document.querySelectorAll('input[type="file"]');
    fileInputs.forEach(input => {
        input.addEventListener('change', function() {
            const maxSize = 50 * 1024 * 1024; // 50MB en bytes
            if (this.files && this.files[0]) {
                if (this.files[0].size > maxSize) {
                    alert("⚠️ El archivo es demasiado grande. El límite máximo es de 50MB.");
                    this.value = ""; // Vaciamos el input para que no se envíe
                }
            }
        });
    });

    // ========================================================================
    // 3. LÓGICA DEL PRESUPUESTO: Auto-Cálculo de IVA y Validación
    // ========================================================================
    const budgetForm = document.getElementById('budgetForm');
    const grossInput = document.getElementById('totalGross');
    const netInput = document.getElementById('totalNet');

    // A. Autocalcular el Neto (Bruto + 21% IVA) automáticamente al escribir
    if (grossInput && netInput) {
        grossInput.addEventListener('input', function() {
            const gross = parseFloat(this.value);
            if (!isNaN(gross) && gross >= 0) {
                const net = gross * 1.21; // Aplicamos el 21% de IVA
                netInput.value = net.toFixed(2);
            } else {
                netInput.value = '';
            }
        });
    }

    // B. Validar antes de enviar que el Neto nunca sea menor que el Bruto
    if (budgetForm) {
        budgetForm.addEventListener('submit', function(event) {
            if (grossInput && netInput) {
                const gross = parseFloat(grossInput.value);
                const net = parseFloat(netInput.value);

                if (net < gross) {
                    alert("❌ Error de lógica comercial: El Total Neto (con IVA) no puede ser inferior al Total Bruto.");
                    event.preventDefault(); // Detenemos el envío al servidor
                }
            }
        });
    }

    // ========================================================================
    // 4. EFECTOS DE USABILIDAD (UX)
    // Auto-ocultar los mensajes de éxito (alertas verdes) tras 5 segundos.
    // ========================================================================
    const successAlerts = document.querySelectorAll('.alert-success');
    successAlerts.forEach(alert => {
        setTimeout(() => {
            // Usamos opacidad para un efecto de desvanecimiento suave (fade out)
            alert.style.transition = "opacity 0.5s ease";
            alert.style.opacity = "0";
            setTimeout(() => alert.remove(), 500); // Lo eliminamos del DOM tras la animación
        }, 5000);
    });

});