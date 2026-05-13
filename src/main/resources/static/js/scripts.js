/**
 * TALLERGO - Scripts globales de la aplicación.
 * Gestiona validaciones de formularios, archivos, alertas y
 * comportamiento general de la navegación.
 */
document.addEventListener("DOMContentLoaded", function () {
    initFormValidation();
    initFileSizeValidation();
    initSuccessAlertsAutoHide();
    initNavbarScroll();
    initDropdowns();
    initMobileMenu();
    initRescheduleModal();
});

/**
 * Inicializa la validación HTML5 de formularios marcados con .needs-validation.
 * Si el formulario no es válido, bloquea el envío y desplaza la vista al primer campo inválido.
 */
function initFormValidation() {
    const forms = document.querySelectorAll(".needs-validation");

    forms.forEach(function (form) {
        form.addEventListener("submit", function (event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();

                const firstInvalidField = form.querySelector(":invalid");
                if (firstInvalidField) {
                    firstInvalidField.scrollIntoView({
                        behavior: "smooth",
                        block: "center"
                    });

                    setTimeout(function () {
                        firstInvalidField.focus();
                    }, 300);
                }
            }

            form.classList.add("was-validated");
        });
    });
}

/**
 * Valida que los archivos seleccionados no superen el tamaño máximo permitido.
 */
function initFileSizeValidation() {
    const fileInputs = document.querySelectorAll('input[type="file"]');
    const maxSize = 50 * 1024 * 1024;
    const fileTooLargeMessage =
        document.body.dataset.fileTooLarge ||
        "El archivo es demasiado grande. El límite máximo es de 50MB.";

    fileInputs.forEach(function (input) {
        input.addEventListener("change", function () {
            if (input.files && input.files[0] && input.files[0].size > maxSize) {
                alert(fileTooLargeMessage);
                input.value = "";
            }
        });
    });
}

/**
 * Oculta automáticamente las alertas de éxito visibles tras unos segundos.
 */
function initSuccessAlertsAutoHide() {
    const successAlerts = document.querySelectorAll(".alert-success, .alert-success-custom");

    successAlerts.forEach(function (alertElement) {
        setTimeout(function () {
            alertElement.style.transition = "opacity 0.5s ease";
            alertElement.style.opacity = "0";

            setTimeout(function () {
                alertElement.remove();
            }, 500);
        }, 5000);
    });
}

/**
 * Añade o quita la clase "scrolled" en la navbar cuando el usuario se desplaza.
 */
function initNavbarScroll() {
    const nav = document.getElementById("tgoNav");
    if (!nav) return;

    function updateNavbarState() {
        nav.classList.toggle("scrolled", window.scrollY > 10);
    }

    updateNavbarState();
    window.addEventListener("scroll", updateNavbarState, { passive: true });
}

/**
 * Gestiona los dropdowns del menú de usuario y del desplegable "Personal".
 */
function initDropdowns() {
    const wrappers = document.querySelectorAll(".tgo-dd-wrap, .tgo-avatar-wrap");
    const buttons = document.querySelectorAll(".tgo-dd-btn, .tgo-avatar-btn");

    if (!wrappers.length || !buttons.length) return;

    function closeAllDropdowns() {
        wrappers.forEach(function (wrapper) {
            wrapper.classList.remove("open");
        });
    }

    buttons.forEach(function (button) {
        button.addEventListener("click", function (event) {
            event.stopPropagation();

            const wrapper = button.closest(".tgo-dd-wrap, .tgo-avatar-wrap");
            if (!wrapper) return;

            const wasOpen = wrapper.classList.contains("open");
            closeAllDropdowns();

            if (!wasOpen) {
                wrapper.classList.add("open");
            }
        });
    });

    document.querySelectorAll(".tgo-dd-menu").forEach(function (menu) {
        menu.addEventListener("click", function (event) {
            event.stopPropagation();
        });
    });

    document.addEventListener("click", function (event) {
        if (!event.target.closest(".tgo-dd-wrap") && !event.target.closest(".tgo-avatar-wrap")) {
            closeAllDropdowns();
        }
    });

    document.addEventListener("keydown", function (event) {
        if (event.key === "Escape") {
            closeAllDropdowns();
        }
    });
}

/**
 * Gestiona la apertura y cierre del menú móvil hamburguesa.
 */
function initMobileMenu() {
    const button = document.getElementById("tgoHamburger");
    const menu = document.getElementById("tgoMobileMenu");
    const nav = document.getElementById("tgoNav");

    if (!button || !menu || !nav) return;

    button.addEventListener("click", function (event) {
        event.stopPropagation();

        const isOpen = menu.classList.toggle("open");
        nav.classList.toggle("menu-open", isOpen);
    });

    document.addEventListener("click", function (event) {
        if (!event.target.closest("#tgoNav")) {
            menu.classList.remove("open");
            nav.classList.remove("menu-open");
        }
    });

    document.addEventListener("keydown", function (event) {
        if (event.key === "Escape") {
            menu.classList.remove("open");
            nav.classList.remove("menu-open");
        }
    });
}

/**
 * Inicializa el modal de reprogramación de citas si existe en la vista.
 */
function initRescheduleModal() {
    const modal = document.getElementById("customRescheduleModal");
    if (!modal) return;

    const dateInput = modal.querySelector('input[name="newDate"]');

    if (dateInput) {
        const tomorrow = new Date();
        tomorrow.setDate(tomorrow.getDate() + 1);
        tomorrow.setMinutes(tomorrow.getMinutes() - tomorrow.getTimezoneOffset());
        dateInput.min = tomorrow.toISOString().slice(0, 16);
    }

    modal.addEventListener("click", function (event) {
        if (event.target === modal) {
            closeRescheduleModal();
        }
    });

    document.addEventListener("keydown", function (event) {
        if (event.key === "Escape" && modal.classList.contains("open")) {
            closeRescheduleModal();
        }
    });
}

/**
 * Abre el modal de reprogramación.
 * Debe quedar en ámbito global porque se usa desde onclick en Thymeleaf.
 */
function openRescheduleModal() {
    const modal = document.getElementById("customRescheduleModal");
    if (modal) {
        modal.classList.add("open");
    }
}

/**
 * Cierra el modal de reprogramación.
 * Debe quedar en ámbito global porque se usa desde onclick en Thymeleaf.
 */
function closeRescheduleModal() {
    const modal = document.getElementById("customRescheduleModal");
    if (modal) {
        modal.classList.remove("open");
    }
}

/**
 * Gestión de eliminación de reseñas.
 */
let reviewFormToDelete = null;

function openDeleteReviewModal(event, formElement) {
    event.preventDefault();
    reviewFormToDelete = formElement;
    const modal = document.getElementById("customReviewDeleteModal");
    if (modal) {
        modal.classList.add("open");
    }
}

function closeDeleteReviewModal() {
    const modal = document.getElementById("customReviewDeleteModal");
    if (modal) {
        modal.classList.remove("open");
    }
    reviewFormToDelete = null;
}

function confirmDeleteReviewAction() {
    if (reviewFormToDelete) {
        reviewFormToDelete.submit();
    }
}