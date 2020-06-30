$(function() {
	checkName();
	var dateFormat = "dd/mm/yy", from = $("#introduced").datepicker({
		defaultDate : "+1w",
		changeMonth : true,
		numberOfMonths : 1,
		dateFormat : dateFormat
	}).on("change", function() {
		to.datepicker("option", "minDate", getDate(this));
	}), to = $("#discontinued").datepicker({
		defaultDate : "+1w",
		changeMonth : true,
		numberOfMonths : 1,
		dateFormat : dateFormat
	}).on("change", function() {
		from.datepicker("option", "maxDate", getDate(this));
	});

	function getDate(element) {
		var date;
		try {
			date = $.datepicker.parseDate(dateFormat, element.value);
		} catch (error) {
			date = null;
		}

		return date;
	}
});

function checkName() {
	$("#createForm").validate({
		rules : {
			computerNameInput : "required"
		},
		messages : {
			computerNameInput : {
				required : "Name required"
			}
		}
	})
}