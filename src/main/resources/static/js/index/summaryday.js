$(function () {
    $(".tcdPageCode").createPage({
        backFn: backFn
    });

    $("#cc").bind("click", function () {
        window.localStorage.clear();
        paramJson = null;
        backFn(1);
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
        url: baseURI + "/ceshi3",
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

var json;

function getForm() {
    var taxno = paramJson == undefined || paramJson == null ? $.trim($("#taxno").val()) : paramJson.taxno;
    var startDate = paramJson == undefined || paramJson == null ? $.trim($("#startDate").val()) : paramJson.startDate;
    var endDate = paramJson == undefined || paramJson == null ? $.trim($("#endDate").val()) : paramJson.endDate;
    $("#taxno").val(taxno);
    $("#startDate").val(startDate);
    $("#endDate").val(endDate);
    json = {
        "pageNo": global_curpage,
        "pageSize": 1,
        "taxno": taxno,
        "startDate": startDate,
        "endDate": endDate
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
            str += '<td>' + (item.createDate == undefined ? "" : item.createDate) + '</td>';
            str += '<td>' + (item.taxNo == undefined ? "" : item.taxNo) + '</td>';
            str += '<td>' + (item.diskNo == undefined ? "" : item.diskNo) + '</td>';
            str += '<td>' + (item.baseData == undefined ? "" : item.baseData) + '</td>';
            str += '<td>' + (item.middData == undefined ? "" : item.middData) + '</td>';
            str += '<td>' + (item.dataData == undefined ? "" : item.dataData) + '</td>';
            str += '<td><button onclick = "jump(\'' + item.taxNo + '\')">详情</button></td>';
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
    alert(taxno);
    // localStorage.setItem("tailaction", "jump");
    // localStorage.setItem("four", JSON.stringify(json));
    window.open(baseURI + "/summarydetail.html")
}