//const today = new Date().toISOString().split('T')[0];
//document.getElementById('date').setAttribute('max', today);

document.addEventListener('DOMContentLoaded', function () {

    var adviceSwitch = document.getElementById('adviceSwitch');
    var adviceText = document.querySelector('.advice-text');

    adviceSwitch.addEventListener('change', function () {
        adviceText.style.display = this.checked ? 'block' : 'none';
    });

    // Get today's date in the format YYYY-MM-DD
        const today = new Date();
        today.setDate(today.getDate() + 1);
        const todayFormatted = today.toISOString().split('T')[0];

        // Get yesterday's date
        const yesterday = new Date();
        yesterday.setDate(yesterday.getDate() - 7);
        const yesterdayFormatted = yesterday.toISOString().split('T')[0];

        // Set the min and max attributes of the date input
        document.getElementById('date').setAttribute('min', yesterdayFormatted);
        document.getElementById('date').setAttribute('max', todayFormatted);

});


function toggleEmergencyCheck() {
    var emergencyCheck = document.getElementById("emergencyCheck");
    var toggleCheckbox = document.getElementById("toggleCheckbox");

    // Toggle the state of toggleCheckbox based on emergencyCheck
    toggleCheckbox.checked = !emergencyCheck.checked;

    // Update other checkboxes based on the state of toggleCheckbox
    updateTargetCheckboxes(toggleCheckbox.checked);

    var checkbox = document.getElementById("emergencyCheck");
    var regularTherapy = document.querySelector(".regular-therapy");

        // Check if the checkbox is checked
        if (checkbox.checked) {
            // If not checked, hide the therapy menu
            regularTherapy.style.display = "none";
        } else {
            // If checked, show the therapy menu
            regularTherapy.style.display = "block";
        }


}

function toggleTargetCheckboxes() {
    // Update other checkboxes based on the state of toggleCheckbox
    var toggleCheckbox = document.getElementById("toggleCheckbox");
    updateTargetCheckboxes(toggleCheckbox.checked);
}

function updateTargetCheckboxes(state) {
    var targetCheckboxes = document.getElementsByClassName("targetCheckbox");
    for (var i = 0; i < targetCheckboxes.length; i++) {
        targetCheckboxes[i].checked = state;
    }
}

function toggleTherapyMenu() {
    var checkbox = document.getElementById("iregularCheck");
    var nameInput = document.getElementById("name");
    var quantityInput = document.getElementById("quantity");
    var therapyMenu = document.querySelector(".therapy-menu");

    // Toggle display of the therapy menu
    therapyMenu.style.display = checkbox.checked ? "block" : "none";

    // Enable or disable inputs based on checkbox state
    nameInput.disabled = !checkbox.checked;
    quantityInput.disabled = !checkbox.checked;

    // Set or remove 'required' attribute based on checkbox state
    if (checkbox.checked) {
        nameInput.setAttribute("required", "required");
        quantityInput.setAttribute("required", "required");
    } else {
        nameInput.removeAttribute("required");
        quantityInput.removeAttribute("required");
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


function showFullText(event, element) {
            // Get the full text
            var fullText = element.textContent.trim(); // Trim removes leading and trailing whitespaces

            // Check if there is content in the note field
            if (fullText !== "") {
                // Get mouse coordinates
                const mouseX = event.clientX;
                const mouseY = event.clientY;

                // Create a modal element
                var modal = document.createElement('div');
                modal.className = 'modal';
                modal.textContent = fullText;

                 // Set modal position right of the cursor
                 modal.style.left = mouseX + 'px';
                 modal.style.top = mouseY + 'px';

                // Append the modal to the body
                document.body.appendChild(modal);

                // Display the modal
                modal.style.display = 'block';

                // Hide the modal on mouseout
                element.onmouseout = function () {
                    document.body.removeChild(modal);
                };
            }
        }

