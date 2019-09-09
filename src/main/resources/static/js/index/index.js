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
    if (_list.countBeans == null || _list.countBeans == "" || _list.countBeans.length == 0) {
        clearDom();
    }else {
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
}

function MD_echarts(_list) {
    if (_list.countBeans == null || _list.countBeans == "" || _list.countBeans.length == 0) {
        clearDom();
    }else {
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
}

function jump(field, params) {
    json.newParam = 'value';
    json.value = params.name;
    json.newParam = 'name';
    json.name = field;
    localStorage.setItem("second", JSON.stringify(json));
    window.open(baseURI + "/summaryview.html")
}

function clearDom() {
    var str = '';
    $("#container").html(str);
    str += '<div id="container1" style="height: 150%;"></div>';
    str += '<div id="container2" style="height: 150%;"></div>';
    $("#container").html(str);
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
        var defaultenddate = new Date();
        defaultenddate.setDate(defaultenddate.getDate()-1);
        var defaultstartdate = new Date();
        defaultstartdate.setMonth(defaultstartdate.getMonth()-1);
        startDate = (defaultstartdate).format("yyyy-MM-dd");
        endDate = (defaultenddate).format("yyyy-MM-dd");
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
        str += '<tr><td colspan="7">';
        str += "数据为空！";
        str += '</td></tr>';
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