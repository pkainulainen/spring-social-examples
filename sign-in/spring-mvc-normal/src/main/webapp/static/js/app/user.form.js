$(function() {
    addValidationErrorClassesToForm();

    function addValidationErrorClassesToForm() {
        $("form").find(".form-group").each(function() {
            var errorMessage = $(this).find(".help-block").text();

            if (errorMessage) {
                $(this).addClass("has-error");
            }
        })
    }
})
