$(function () {
    $(".tcdPageCode").createPage({
        backFn:backFn
    });

    $("#cc").bind("click", function () {
        backFn(1);
    });

    backFn(1);
})
var global_curpage = 1;

function backFn(_curPage) {
    global_curpage = _curPage;
    queryList(function (_response) {
        renderTableListHTML(_response);
    });
};

function queryList(myCallBack) {
    var json = getForm();
    // var json = {
    //     "pageNo": global_curpage,
    //     "pageSize": 10,
    //     "id": 0,
    //     "contractCode": "",
    //     "createTime": ""
    // };
    // $(".loading").css("display","block");
    $.ajax({
        url: baseURI + "/ceshi",
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
    var id = $.trim($("#id").val()) == "" ? 0 : $.trim($("#id").val());
    var contractCode = $.trim($("#contractCode").val());
    var createTime = $.trim($("#createTime").val());
    var json = {
        "pageNo": global_curpage,
        "pageSize": 1,
        "id": id,
        "contractCode": contractCode,
        "createTime": createTime
    };
    return json;
}

//渲染
function renderTableListHTML(_list) {
    var str = '';
    $("#contentHTML").empty();
    if (_list.list == null || _list.list == "") {
        str = "数据为空！";
    } else {
        for (var i = 0; i < _list.list.length; i++) {
            var item = _list.list[i];
            str += '<tr>';
            str += '<td>' + (item.id == undefined ? "" : item.id) + '</td>';
            str += '<td>' + (item.contractCode == undefined ? "" : item.contractCode) + '</td>';
            str += '<td>' + (item.createTime == undefined ? "" : item.createTime) + '</td>';
            str += '</tr>';

        }
    }
    $("#contentHTML").html(str);

    var page = _list.page;
    var pageCount = page.pageCount;
    var curPage = page.curPage;
//	//分页控件
    $(".tcdPageCode").createPage({
        pageCount:pageCount,
        current:curPage
    });
    return ;
};