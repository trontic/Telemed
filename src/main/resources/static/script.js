const today = new Date().toISOString().split('T')[0];
document.getElementById('date').setAttribute('max', today);

document.addEventListener('DOMContentLoaded', function () {

    var adviceSwitch = document.getElementById('adviceSwitch');
    var adviceText = document.querySelector('.advice-text');

    adviceSwitch.addEventListener('change', function () {
        adviceText.style.display = this.checked ? 'block' : 'none';
    });
});