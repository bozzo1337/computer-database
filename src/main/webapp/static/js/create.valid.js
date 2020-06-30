$(document).ready(function() {
	initIntroDatePicker();
	initDiscDatePicker();
	checkName();
});

function initIntroDatePicker() {
	$("#introduced").datepicker({
		dateFormat : 'mm/dd/yy'
	})
}

function initDiscDatePicker() {
	$("#discontinued").datepicker({
		dateFormat : 'mm/dd/yy'
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