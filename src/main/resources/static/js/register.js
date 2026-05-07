document.addEventListener("DOMContentLoaded", function() {

    // 1. Mostrar/Ocultar contraseñas
    const toggles = [
        { btn: 'togglePassword', input: 'password' },
        { btn: 'toggleConfirm', input: 'confirmPassword' }
    ];

    toggles.forEach(({btn, input}) => {
        const buttonEl = document.getElementById(btn);
        const inputEl = document.getElementById(input);

        if (buttonEl && inputEl) {
            buttonEl.addEventListener('click', () => {
                const isPassword = inputEl.type === 'password';
                inputEl.type = isPassword ? 'text' : 'password';
                buttonEl.className = isPassword ? 'bi bi-eye-slash input-icon' : 'bi bi-eye input-icon';
            });
        }
    });

    // 2. Efecto Parallax en el fondo
    const hero = document.querySelector('.register-hero');
    if (hero) {
        document.addEventListener('mousemove', (e) => {
            const xPct = (e.clientX / window.innerWidth  - 0.5) * 6;
            const yPct = (e.clientY / window.innerHeight - 0.5) * 4;
            hero.style.backgroundPosition = `calc(50% + ${xPct}px) calc(40% + ${yPct}px)`;
        });
    }
});