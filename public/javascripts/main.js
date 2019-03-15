
$(document).ready(function() {
    $(".mybtn").bind('input', function() {
        var id, text;
        id = $(this).attr("id");
        text =  $(this).val()

        console.log($(this).val());
        $.ajax({
            url: "update",
            type: "PUT",
            data: {"id": Number.parseInt(id), "text": text},
            success: function ( data, textStatus, jqXHR) {
                console.log( 'Item updated: ' + id + ', ' + textStatus );
            },
            error: function ( jqXHR, textStatus, errorThrown ) {
                console.error( 'Could not update item: ' + id + ', due to: ' + textStatus + ' | ' + errorThrown);
            }
        });
    });

    $(".btnDeleteTask").click(function () {
        var id = $(this).attr('id');
        $.ajax({
            url: "delete/" + id,
            type: "DELETE",
            success: function ( data, textStatus, jqXHR) {
                window.location.href = "/";
                console.log( 'Item updated: ' + id + ', ' + textStatus );
            },
            error: function ( jqXHR, textStatus, errorThrown ) {
                console.error( 'Could not update item: ' + id + ', due to: ' + textStatus + ' | ' + errorThrown);
            }
        });
    });


});