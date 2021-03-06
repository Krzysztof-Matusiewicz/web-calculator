
var twoArgumentOperators = ["+", "-", "*", "/", "^"];

var info = {
    update : function(value) {
        $("#info").html(value);
    },
    clear : function() {
        info.update("");
    },
    help : {
        integral: function() {
            info.update("<b>\u222Be<sup>x</sup> syntax:</b> \u222B(lowerBound;upperBound;intervals;threads)");
        },
        power: function() {
            info.update("<b>x<sup>y</sup> syntax:</b> x^y");
        }
    }
};

var input = {
    getValue : function() {
        return $("#input").val()
    },
    setValue : function(value) {
        $("#input").val(value);
    },
    clearIfContainsZero : function(newCharacter) {
        if (input.getValue() == "0" && twoArgumentOperators.indexOf(newCharacter) < 0) {
            input.setValue("");
        }
    },
    append : function(value) {
        input.clearIfContainsZero(value);
        input.setValue(input.getValue() + value);
        info.clear();
    },
    removeLastOutputCharacter : function() {
        var value = input.getValue();
        if (value) {
            value = value.slice(0, -1);
            if (!value) {
                value = "0";
            }
            input.setValue(value);
            info.clear();
        }
    }
};

var historyPanel = {
    isVisible : function() {
        return $("#historyContainer").is(":visible");
    },
    update : function() {
        $.getJSON("/history")
            .done(function(data) {
                var historyHtml = "";
                $.each(data.history.items.reverse(), function(i, item){
                    historyHtml += "<li>" + calc.toDomain(item.expression) + " = " + item.result + "</li>";
                });
                $("#history").html(historyHtml);
            }).fail(function() {
            info.update("Cannot read calculation history");
        });
    },
    toggle : function() {
        if (!historyPanel.isVisible()) {
            historyPanel.update();
        }
        $("#historyContainer").toggle();
    }
};

var calc = {
    squareRoot : function() {
        input.append("\u221A(");
    },
    integral : function() {
        input.append("\u222B(");
    },
    clear : function() {
        input.setValue("0");
        info.clear();
    },
    toDto : function(value) {
        return value.replace("\u221A", "sqrt")
                    .replace("\u222B", "integral")
    },
    toDomain : function(value) {
        return value.replace("sqrt", "\u221A")
                    .replace("integral", "\u222B");
    },
    execute : function() {
        $.getJSON("/calculate", {
            exp: calc.toDto(input.getValue())
        }).done(function(data) {
            info.update(input.getValue());
            input.setValue(String(data.result));
            if (historyPanel.isVisible()) {
                historyPanel.update();
            }
        }).fail(function(jqXHR) {
            var data = jQuery.parseJSON(jqXHR.responseText);
            info.update(data.message);
        });
    }
};

function init() {
    $("td.inputCharacter").bind("click", function () {
        input.append($(this).text());
    });
    $("td.squareRoot").bind("click", calc.squareRoot);
    $("td.integral").bind("click", calc.integral);
    $("td.integral").hover(info.help.integral, info.clear);
    $("td.power").hover(info.help.power, info.clear);
    $("td.backspace").bind("click", input.removeLastOutputCharacter);
    $("td.calculate").bind("click", calc.execute);
    $("td.history").bind("click", historyPanel.toggle);
    $("td.clear").bind("click", calc.clear);
    calc.clear();
    $("#historyContainer").hide();
}

$(document).ready(init);
