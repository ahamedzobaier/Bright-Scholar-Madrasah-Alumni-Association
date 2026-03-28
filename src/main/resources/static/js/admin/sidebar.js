document.addEventListener('DOMContentLoaded', () => {
    const menuBtn = document.getElementById('menu-toggle');
    const body = document.body;

    menuBtn.addEventListener('click', () => {
        // For Desktop: Toggle the 'sidebar-hidden' class
        if (window.innerWidth > 1024) {
            body.classList.toggle('sidebar-hidden');
        } 
        // For Mobile: Toggle the 'sidebar-active' class
        else {
            body.classList.toggle('sidebar-active');
        }
    });

    // Close sidebar when clicking outside on mobile
    document.addEventListener('click', (e) => {
        if (body.classList.contains('sidebar-active') && 
            !e.target.closest('#sidebar') && 
            !e.target.closest('#menu-toggle')) {
            body.classList.remove('sidebar-active');
        }
    });
});