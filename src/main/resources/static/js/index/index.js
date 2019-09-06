$(function () {
    $(".tcdPageCode").createPage({
        backFn: backFn
    });

    $("#cc").bind("click", function () {
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
        BM_echarts(_response);
        MD_echarts(_response);
    });
};

function BM_echarts(_list) {
    var bmAreas = _list.bmAreas;
    var datas = new Array()
    for (var i = 0; i < bmAreas.length; i++) {
        datas[i] = bmAreas[i];
    }
    var dom = document.getElementById("container1");
    var myChart = echarts.init(dom);
    option = null;
    option = {
        title: {
            text: '数据比对',
            subtext: '原始/中间件',
            x: 'center'
        },
        tooltip: {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        // legend: {
        //     orient: 'vertical',
        //     left: 'left',
        //     data: ['直接访问','邮件营销','联盟广告','视频广告','搜索引擎']
        // },
        series: [
            {
                name: '访问来源',
                type: 'pie',
                radius: '55%',
                center: ['50%', '60%'],
                data: datas,
                itemStyle: {
                    emphasis: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ]
    };

    if (option && typeof option === "object") {
        myChart.setOption(option, true);
    }
    //防止重复触发点击事件
    if (myChart._$handlers.click) {
        myChart._$handlers.click.length = 0;
    }
    myChart.on('click', function (params) {
        jump("bm", params);
    });
}

function jump(field, params) {
    json.newParam = 'value';
    json.value = params.name;
    json.newParam = 'name';
    json.name = field;
    localStorage.setItem("second", JSON.stringify(json));
    window.open(baseURI + "/summaryview.html")
}

function MD_echarts(_list) {
    var mdAreas = _list.mdAreas;
    var datas = new Array()
    for (var i = 0; i < mdAreas.length; i++) {
        datas[i] = mdAreas[i];
    }
    var dom = document.getElementById("container2");
    var myChart = echarts.init(dom);
    option = null;
    option = {
        title: {
            text: '数据比对',
            subtext: '中间件/大数据',
            x: 'center'
        },
        tooltip: {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        // legend: {
        //     orient: 'vertical',
        //     left: 'left',
        //     data: ['100%', '>=80%', '>=60%', '<60%']
        // },
        series: [
            {
                name: '访问来源',
                type: 'pie',
                radius: '55%',
                center: ['50%', '60%'],
                data: datas,
                itemStyle: {
                    emphasis: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ]
    };
    if (option && typeof option === "object") {
        myChart.setOption(option, false);
    }
    //防止重复触发点击事件
    if (myChart._$handlers.click) {
        myChart._$handlers.click.length = 0;
    }
    myChart.on('click', function (params) {
        jump("md", params);
    });
}

var json = {};

function queryList(myCallBack) {
    json = getForm();
    // $(".loading").css("display","block");
    $.ajax({
        url: baseURI + "/ceshi1",
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

var startDate;
var endDate;

function getForm() {
    var taxno = $.trim($("#taxno").val());
    startDate = $.trim($("#startDate").val());
    endDate = $.trim($("#endDate").val());

    if (startDate == "" && endDate == "") {
        startDate = "2019-08-15";
        endDate = "2019-09-05";
    }


    var json = {
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
            str += '<td>' + (item.taxno == undefined ? "" : item.taxno) + '</td>';
            str += '<td>' + (item.diskno == undefined ? "" : item.diskno) + '</td>';
            str += '<td>' + (item.spcount == undefined ? "" : item.spcount) + '</td>';
            str += '<td>' + (item.midcount == undefined ? "" : item.midcount) + '</td>';
            str += '<td>' + (item.dsjcount == undefined ? "" : item.dsjcount) + '</td>';
            str += '<td>' + startDate + '</td>';
            str += '<td>' + endDate + '</td>';
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