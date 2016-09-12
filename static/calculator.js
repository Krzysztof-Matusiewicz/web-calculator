
var output = "";
var twoArgumentOperators = ["+", "-", "*", "/", "^"];

function addToOutput(value) {
    clearOutputIfContainsZero(value);
    output += value;
    updateOutput();
    clearMessage();
}

function clearOutputIfContainsZero(newCharacter) {
    if (output == "0" && twoArgumentOperators.indexOf(newCharacter) < 0) {
        output = "";
        updateOutput();
    }
}

function squareRoot() {
    addToOutput("sqrt(");
}

function integral() {
    addToOutput("integral(");
}

function removeLastOutputCharacter() {
    if (output) {
        if (output)
        output = output.slice(0, -1);
        if (!output) {
            output = "0";
        }
        updateOutput();
        clearMessage();
    }
}

function clear() {
    output = "0";
    updateOutput();
    clearMessage();
}

function calculate() {
    $.getJSON("/calculate", {
        exp: output
    }).done(function(data) {
        updateMessage(output);
        output = String(data.result);
        updateOutput();
        if (isHistoryVisible()) {
            updateHistory();
        }
    }).fail(function(jqXHR) {
        var data = jQuery.parseJSON(jqXHR.responseText);
        updateMessage(data.message);
    });
}

function isHistoryVisible() {
    return $("#historyContainer").is(":visible");
}

function history() {
    if (!isHistoryVisible()) {
        updateHistory();
    }
    $("#historyContainer").toggle();
}

function updateHistory() {
    $.getJSON("/history", {
        exp: output
    }).done(function(data) {
        var historyHtml = "";
        $.each(data.history.items.reverse(), function(i, item){
            historyHtml += "<li>" + item.expression + " = " + item.result + "</li>";
        });
        $("#history").html(historyHtml);
    }).fail(function() {
        updateMessage("Cannot read calculation history");
    });
}

function updateOutput() {
    $("#result").text(output);
}

function updateMessage(value) {
    $("#message").html(value);
}

function clearMessage() {
    updateMessage("");
}

function init() {
    $("td.inputCharacter").bind("click", function () {
        addToOutput($(this).text());
    });
    $("td.squareRoot").bind("click", squareRoot);
    $("td.integral").bind("click", integral);
    $("td.backspace").bind("click", removeLastOutputCharacter);
    $("td.calculate").bind("click", calculate);
    $("td.history").bind("click", history);
    $("td.clear").bind("click", clear);
    clear();
    $("#historyContainer").hide();
}

$(document).ready(init);
