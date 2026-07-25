/**
 * BookMyShow Interactive Seat Selection Matrix Script
 */
document.addEventListener("DOMContentLoaded", function () {
    const seatBoxes = document.querySelectorAll(".seat-box:not(.booked)");
    const selectedSeatsInput = document.getElementById("selectedSeatsInput");
    const selectedSeatsDisplay = document.getElementById("selectedSeatsDisplay");
    const totalAmountDisplay = document.getElementById("totalAmountDisplay");
    const seatCountDisplay = document.getElementById("seatCountDisplay");
    const checkoutBtn = document.getElementById("checkoutBtn");

    let selectedSeats = [];

    seatBoxes.forEach(seat => {
        seat.addEventListener("click", function () {
            const seatNum = this.getAttribute("data-seat");
            const price = parseFloat(this.getAttribute("data-price") || "200");

            if (this.classList.contains("selected")) {
                // Deselect
                this.classList.remove("selected");
                selectedSeats = selectedSeats.filter(s => s.code !== seatNum);
            } else {
                // Limit to 10 seats per booking max
                if (selectedSeats.length >= 10) {
                    alert("You can select a maximum of 10 seats per booking.");
                    return;
                }
                // Select
                this.classList.add("selected");
                selectedSeats.push({ code: seatNum, price: price });
            }

            updateSummary();
        });
    });

    function updateSummary() {
        if (selectedSeats.length === 0) {
            if (selectedSeatsDisplay) selectedSeatsDisplay.textContent = "None";
            if (seatCountDisplay) seatCountDisplay.textContent = "0";
            if (totalAmountDisplay) totalAmountDisplay.textContent = "₹ 0.00";
            if (selectedSeatsInput) selectedSeatsInput.value = "";
            if (checkoutBtn) checkoutBtn.disabled = true;
        } else {
            const seatCodes = selectedSeats.map(s => s.code);
            const subtotal = selectedSeats.reduce((acc, curr) => acc + curr.price, 0);
            const fees = 35.40 * selectedSeats.length;
            const grandTotal = subtotal + fees;

            if (selectedSeatsDisplay) selectedSeatsDisplay.textContent = seatCodes.join(", ");
            if (seatCountDisplay) seatCountDisplay.textContent = selectedSeats.length;
            if (totalAmountDisplay) totalAmountDisplay.textContent = "₹ " + grandTotal.toFixed(2) + " (incl. taxes)";
            if (selectedSeatsInput) selectedSeatsInput.value = seatCodes.join(",");
            if (checkoutBtn) checkoutBtn.disabled = false;
        }
    }
});
