$(function () {
    $(".tcdPageCode").createPage({
        backFn: backFn
    });

    backFn(1);
})
var global_curpage = 1;

function backFn(_curPage) {
    if (!isNaN(_curPage) && typeof _curPage === 'number')
        global_curpage = _curPage;
    queryList(function (_response) {
        renderTableListHTML(_response);
    });
};

var json = {};

function queryList(myCallBack) {
    json = getForm();
    // $(".loading").css("display","block");
    $.ajax({
        url: baseURI + "/ceshi2",
        type: "POST",
        dataType: "json",
        contentType: 'application/json;charset=UTF-8',
        // beforeSend: function(request) {
        //     request.setRequestHeader("token", xtoken);
        // },
        data: JSON.stringify(json),
        async: true,
        cache: false,
        success: function (data) {
            if (myCallBack) {
                myCallBack(data);
            }
            // $(".loading").css("display","none");
        }
    });
    return;
}

function getForm() {
    var json = {
        "pageNo": global_curpage,
        "pageSize": 1,
        "name": paramJson.name,
        "value": paramJson.value,
        "taxno": paramJson.taxno,
        "startDate": paramJson.startDate,
        "endDate": paramJson.endDate
    };
    return json;
}

//渲染
function renderTableListHTML(_list) {
    var str = '';
    $("#contentHTML").empty();
    if (_list.countBeans == null || _list.countBeans == "") {
        str = "数据为空！";
    } else {
        for (var i = 0; i < _list.countBeans.length; i++) {
            var item = _list.countBeans[i];
            str += '<tr>';
            str += '<td onclick="jump(\'' + item.taxno + '\')">' + (item.taxno == undefined ? "" : item.taxno) + '</td>';
            str += '<td>' + (item.diskno == undefined ? "" : item.diskno) + '</td>';
            str += '<td>' + (item.spcount == undefined ? "" : item.spcount) + '</td>';
            str += '<td>' + (item.midcount == undefined ? "" : item.midcount) + '</td>';
            str += '<td>' + (item.dsjcount == undefined ? "" : item.dsjcount) + '</td>';
            str += '<td>' + paramJson.startDate + '</td>';
            str += '<td>' + paramJson.endDate + '</td>';
            str += '</tr>';
        }
    }
    $("#contentHTML").html(str);

    var page = _list.page;
    var pageCount = page.pageCount;
    var curPage = page.curPage;
//	//分页控件
    $(".tcdPageCode").createPage({
        pageCount: pageCount,
        current: curPage
    });
    return;
};

function jump(taxno) {
    var third = {}
    third.newParam = 'taxno';
    third.taxno = taxno;
    third.newParam = 'startDate';
    third.startDate = paramJson.startDate;
    third.newParam = 'endDate';
    third.endDate = paramJson.endDate;
    localStorage.setItem("action", "jump");
    localStorage.setItem("third", JSON.stringify(third));
    window.open(baseURI + "/summaryday.html")
}