$(document).ready(function() {
	initDatePicker();
	checkName();
});

function initDatePicker() {
	$("#introduced").datepicker({
		dateFormat : 'dd/mm/yy'
	})
}

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