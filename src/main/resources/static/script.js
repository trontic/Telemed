const today = new Date().toISOString().split('T')[0];
document.getElementById('date').setAttribute('max', today);

document.addEventListener('DOMContentLoaded', function () {

    var adviceSwitch = document.getElementById('adviceSwitch');
    var adviceText = document.querySelector('.advice-text');

    adviceSwitch.addEventListener('change', function () {
        adviceText.style.display = this.checked ? 'block' : 'none';
    });
});

function toggleTargetCheckboxes() {
    var toggle = document.getElementById("toggleCheckbox");
    var targetCheckboxes = document.getElementsByClassName("targetCheckbox");

    for (var i = 0; i < targetCheckboxes.length; i++) {
      targetCheckboxes[i].checked = toggle.checked
    }
  }

  function myFunction() {
    var x = document.getElementById("myLinks");
    if (x.style.display === "block") {
      x.style.display = "none";
    } else {
      x.style.display = "block";
    }
  }