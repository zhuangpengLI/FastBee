(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-bcc438f2"],{"01ca":function(e,t,i){"use strict";i.d(t,"f",(function(){return o})),i.d(t,"d",(function(){return a})),i.d(t,"a",(function(){return r})),i.d(t,"e",(function(){return s})),i.d(t,"g",(function(){return l})),i.d(t,"c",(function(){return c})),i.d(t,"b",(function(){return d}));var n=i("b775");function o(e){return Object(n["a"])({url:"/iot/model/list",method:"get",params:e})}function a(e){return Object(n["a"])({url:"/iot/model/"+e,method:"get"})}function r(e){return Object(n["a"])({url:"/iot/model",method:"post",data:e})}function s(e){return Object(n["a"])({url:"/iot/model/import",method:"post",data:e})}function l(e){return Object(n["a"])({url:"/iot/model",method:"put",data:e})}function c(e){return Object(n["a"])({url:"/iot/model/"+e,method:"delete"})}function d(e){return Object(n["a"])({url:"/iot/model/cache/"+e,method:"get"})}},"584f":function(e,t,i){"use strict";i.d(t,"l",(function(){return o})),i.d(t,"j",(function(){return a})),i.d(t,"k",(function(){return r})),i.d(t,"i",(function(){return s})),i.d(t,"e",(function(){return l})),i.d(t,"c",(function(){return c})),i.d(t,"f",(function(){return d})),i.d(t,"h",(function(){return u})),i.d(t,"g",(function(){return f})),i.d(t,"a",(function(){return m})),i.d(t,"m",(function(){return v})),i.d(t,"b",(function(){return p})),i.d(t,"d",(function(){return h}));var n=i("b775");function o(e){return Object(n["a"])({url:"/iot/device/unAuthlist",method:"get",params:e})}function a(e){return Object(n["a"])({url:"/iot/device/listByGroup",method:"get",params:e})}function r(e){return Object(n["a"])({url:"/iot/device/shortList",method:"get",params:e})}function s(){return Object(n["a"])({url:"/iot/device/all",method:"get"})}function l(e){return Object(n["a"])({url:"/iot/device/"+e,method:"get"})}function c(e){return Object(n["a"])({url:"/iot/device/synchronization/"+e,method:"get"})}function d(e){return Object(n["a"])({url:"/iot/device/getDeviceBySerialNumber/"+e,method:"get"})}function u(){return Object(n["a"])({url:"/iot/device/statistic",method:"get"})}function f(e){return Object(n["a"])({url:"/iot/device/runningStatus/"+e,method:"get"})}function m(e){return Object(n["a"])({url:"/iot/device",method:"post",data:e})}function v(e){return Object(n["a"])({url:"/iot/device",method:"put",data:e})}function p(e){return Object(n["a"])({url:"/iot/device/"+e,method:"delete"})}function h(){return Object(n["a"])({url:"/iot/device/generator",method:"get"})}},"5f43":function(e,t,i){"use strict";i.r(t);var n=function(){var e=this,t=e.$createElement,i=e._self._c||t;return i("div",{staticStyle:{"padding-left":"20px"}},[i("el-row",{attrs:{gutter:80}},[i("el-col",{attrs:{xs:24,sm:24,md:24,lg:10,xl:10}},[i("el-descriptions",{attrs:{column:1,border:"",title:e.title}},[i("el-descriptions-item",{attrs:{labelStyle:e.statusColor}},[i("template",{slot:"label"},[i("svg-icon",{attrs:{"icon-class":"ota"}}),e._v(" OTA升级 ")],1),i("el-link",{staticStyle:{"line-height":"28px","font-size":"16px","padding-right":"10px"},attrs:{underline:!1}},[e._v("Version "+e._s(e.deviceInfo.firmwareVersion))]),i("el-button",{staticStyle:{float:"right"},attrs:{type:"success",size:"mini",disabled:3!=e.deviceInfo.status},on:{click:function(t){return e.getLatestFirmware(e.deviceInfo.deviceId)}}},[e._v("检查更新")])],2),e._l(e.deviceInfo.boolList,(function(t,n){return i("el-descriptions-item",{key:n,attrs:{labelStyle:e.statusColor}},[i("template",{slot:"label"},[i("i",{staticClass:"el-icon-open"}),e._v(" "+e._s(t.name)+" ")]),i("el-switch",{staticStyle:{"min-width":"100px"},attrs:{"active-text":"","inactive-text":"","active-value":"1","inactive-value":"0",disabled:e.shadowUnEnable},on:{change:function(i){return e.publishThingsModel(e.deviceInfo,t)}},model:{value:t.shadow,callback:function(i){e.$set(t,"shadow",i)},expression:"item.shadow"}})],2)})),e._l(e.deviceInfo.enumList,(function(t,n){return i("el-descriptions-item",{key:n,attrs:{labelStyle:e.statusColor}},[i("template",{slot:"label"},[i("i",{staticClass:"el-icon-s-unfold"}),e._v(" "+e._s(t.name)+" ")]),i("el-select",{attrs:{placeholder:"请选择",clearable:"",disabled:e.shadowUnEnable},on:{change:function(i){return e.publishThingsModel(e.deviceInfo,t)}},model:{value:t.shadow,callback:function(i){e.$set(t,"shadow",i)},expression:"item.shadow"}},e._l(t.enumList,(function(e){return i("el-option",{key:e.value,attrs:{label:e.text,value:e.value}})})),1)],2)})),e._l(e.deviceInfo.stringList,(function(t,n){return i("el-descriptions-item",{key:n,attrs:{labelStyle:e.statusColor}},[i("template",{slot:"label"},[i("i",{staticClass:"el-icon-tickets"}),e._v(" "+e._s(t.name)+" ")]),i("el-input",{attrs:{placeholder:"请输入字符串",disabled:e.shadowUnEnable},model:{value:t.shadow,callback:function(i){e.$set(t,"shadow",i)},expression:"item.shadow"}},[i("el-button",{staticStyle:{"font-size":"20px"},attrs:{slot:"append",icon:"el-icon-s-promotion",title:"指令发送"},on:{click:function(i){return e.publishThingsModel(e.deviceInfo,t)}},slot:"append"})],1)],2)})),e._l(e.deviceInfo.arrayList,(function(t,n){return i("el-descriptions-item",{key:n,attrs:{labelStyle:e.statusColor}},[i("template",{slot:"label"},[i("i",{staticClass:"el-icon-tickets"}),e._v(" "+e._s(t.name)+" ")]),i("el-input",{attrs:{placeholder:"请输入英文逗号分隔的字符串",disabled:e.shadowUnEnable},model:{value:t.shadow,callback:function(i){e.$set(t,"shadow",i)},expression:"item.shadow"}},[i("el-button",{staticStyle:{"font-size":"20px"},attrs:{slot:"append",icon:"el-icon-s-promotion",title:"指令发送"},on:{click:function(i){return e.publishThingsModel(e.deviceInfo,t)}},slot:"append"})],1)],2)})),e._l(e.deviceInfo.decimalList,(function(t,n){return i("el-descriptions-item",{key:n,attrs:{labelStyle:e.statusColor}},[i("template",{slot:"label"},[i("i",{staticClass:"el-icon-star-off"}),e._v(" "+e._s(t.name)+" ")]),i("el-input",{attrs:{type:"number",placeholder:"请输入小数 ",disabled:e.shadowUnEnable},model:{value:t.shadow,callback:function(i){e.$set(t,"shadow",i)},expression:"item.shadow"}},[i("el-button",{staticStyle:{"font-size":"20px"},attrs:{slot:"append",icon:"el-icon-s-promotion",title:"指令发送"},on:{click:function(i){return e.publishThingsModel(e.deviceInfo,t)}},slot:"append"})],1)],2)})),e._l(e.deviceInfo.integerList,(function(t,n){return i("el-descriptions-item",{key:n,attrs:{labelStyle:e.statusColor}},[i("template",{slot:"label"},[i("i",{staticClass:"el-icon-paperclip"}),e._v(" "+e._s(t.name)+" ")]),i("el-input",{attrs:{type:"integer",placeholder:"请输入整数 ",disabled:e.shadowUnEnable},model:{value:t.shadow,callback:function(i){e.$set(t,"shadow",i)},expression:"item.shadow"}},[i("el-button",{staticStyle:{"font-size":"20px"},attrs:{slot:"append",icon:"el-icon-s-promotion",title:"指令发送"},on:{click:function(i){return e.publishThingsModel(e.deviceInfo,t)}},slot:"append"})],1)],2)}))],2),i("el-descriptions",{staticStyle:{margin:"40px 0"},attrs:{column:2,border:"",title:"监测数据"}},e._l(e.deviceInfo.readOnlyList,(function(t,n){return i("el-descriptions-item",{key:n},[i("template",{slot:"label"},[i("i",{staticClass:"el-icon-odometer"}),e._v(" "+e._s(t.name)+" ")]),i("el-link",{attrs:{type:"primary",underline:!1}},[e._v(e._s(t.shadow)+" "+e._s(null==t.unit?"":t.unit))])],2)})),1),1==e.deviceInfo.isShadow&&3!=e.deviceInfo.status?i("el-descriptions",{attrs:{column:1,border:"",size:"mini"}},[i("template",{slot:"title"},[i("span",{staticStyle:{"font-size":"14px",color:"#606266"}},[e._v("设备离线时状态")])]),e._l(e.deviceInfo.boolList,(function(t,n){return i("el-descriptions-item",{key:n},[i("template",{slot:"label"},[i("i",{staticClass:"el-icon-open"}),e._v(" "+e._s(t.name)+" ")]),i("el-switch",{staticStyle:{"min-width":"100px"},attrs:{size:"mini","active-text":"","inactive-text":"","active-value":"1","inactive-value":"0",disabled:""},model:{value:t.value,callback:function(i){e.$set(t,"value",i)},expression:"item.value"}})],2)})),e._l(e.deviceInfo.enumList,(function(t,n){return i("el-descriptions-item",{key:n},[i("template",{slot:"label"},[i("i",{staticClass:"el-icon-s-unfold"}),e._v(" "+e._s(t.name)+" ")]),i("el-select",{attrs:{placeholder:"请选择",clearable:"",size:"mini",disabled:""},model:{value:t.value,callback:function(i){e.$set(t,"value",i)},expression:"item.value"}},e._l(t.enumList,(function(e){return i("el-option",{key:e.value,attrs:{label:e.text,value:e.value}})})),1)],2)})),e._l(e.deviceInfo.stringList,(function(t,n){return i("el-descriptions-item",{key:n},[i("template",{slot:"label"},[i("i",{staticClass:"el-icon-tickets"}),e._v(" "+e._s(t.name)+" ")]),e._v(" "+e._s(t.value)+" "+e._s(t.unit?t.unit:"")+" ")],2)})),e._l(e.deviceInfo.arrayList,(function(t,n){return i("el-descriptions-item",{key:n},[i("template",{slot:"label"},[i("i",{staticClass:"el-icon-tickets"}),e._v(" "+e._s(t.name)+" ")]),e._v(" "+e._s(t.value)+" "+e._s(t.unit?t.unit:"")+" ")],2)})),e._l(e.deviceInfo.decimalList,(function(t,n){return i("el-descriptions-item",{key:n},[i("template",{slot:"label"},[i("i",{staticClass:"el-icon-star-off"}),e._v(" "+e._s(t.name)+" ")]),e._v(" "+e._s(t.value)+" "+e._s(t.unit?t.unit:"")+" ")],2)})),e._l(e.deviceInfo.integerList,(function(t,n){return i("el-descriptions-item",{key:n},[i("template",{slot:"label"},[i("i",{staticClass:"el-icon-paperclip"}),e._v(" "+e._s(t.name)+" ")]),e._v(" "+e._s(t.value)+" "+e._s(t.unit?t.unit:"")+" ")],2)}))],2):e._e()],1),e.deviceInfo.readOnlyList.length>0?i("el-col",{attrs:{xs:24,sm:24,md:24,lg:14,xl:14}},[i("el-row",{staticStyle:{"background-color":"#F5F7FA",padding:"20px 20px 10px 10px","border-radius":"15px","margin-right":"5px"},attrs:{gutter:20}},e._l(e.deviceInfo.readOnlyList,(function(e,t){return i("el-col",{key:t,attrs:{xs:24,sm:12,md:12,lg:12,xl:8}},[i("el-card",{staticStyle:{"border-radius":"30px","margin-bottom":"20px"},attrs:{shadow:"hover"}},[i("div",{ref:"map",refInFor:!0,staticStyle:{height:"230px",width:"180px",margin:"0 auto"}})])],1)})),1)],1):e._e()],1),i("el-dialog",{attrs:{title:"设备固件升级",visible:e.openFirmware,width:"600px","append-to-body":""},on:{"update:visible":function(t){e.openFirmware=t}}},[null==e.firmware||e.deviceInfo.firmwareVersion>=e.firmware.version?i("div",{staticStyle:{"text-align":"center","font-size":"16px"}},[i("i",{staticClass:"el-icon-success",staticStyle:{color:"#67C23A"}}),e._v(" 已经是最新版本，不需要升级")]):e._e(),null!=e.firmware&&e.deviceInfo.firmwareVersion<e.firmware.version?i("el-descriptions",{attrs:{column:1,border:"",size:"large",labelStyle:{width:"100px","font-weight":"bold"}}},[i("template",{slot:"title"},[i("el-link",{attrs:{icon:"el-icon-success",type:"success",underline:!1}},[e._v(" 可以升级到以下版本")])],1),i("el-descriptions-item",{attrs:{label:"固件名称"}},[e._v(e._s(e.firmware.firmwareName))]),i("el-descriptions-item",{attrs:{label:"所属产品"}},[e._v(e._s(e.firmware.productName))]),i("el-descriptions-item",{attrs:{label:"固件版本"}},[e._v("Version "+e._s(e.firmware.version))]),i("el-descriptions-item",{attrs:{label:"下载地址"}},[i("el-link",{attrs:{href:e.getDownloadUrl(e.firmware.filePath),underline:!1,type:"primary"}},[e._v(e._s(e.getDownloadUrl(e.firmware.filePath)))])],1),i("el-descriptions-item",{attrs:{label:"固件描述"}},[e._v(e._s(e.firmware.remark))])],2):e._e(),i("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[null!=e.firmware&&e.deviceInfo.firmwareVersion<e.firmware.version?i("el-button",{attrs:{type:"success"},on:{click:e.otaUpgrade}},[e._v("升 级")]):e._e(),i("el-button",{on:{click:e.cancel}},[e._v("取 消")])],1)],1)],1)},o=[],a=i("c7eb"),r=i("1da1"),s=(i("b64b"),i("d3b7"),i("25f0"),i("b0c0"),i("d81d"),i("584f")),l=i("814a"),c=i("01ca"),d=i("313e"),u={name:"running-status",components:{},props:{device:{type:Object,default:null}},watch:{device:function(e,t){var i=this;e&&0!=e.deviceId&&(Object(s["g"])(e.deviceId).then((function(e){i.deviceInfo=e.data,i.updateDeviceStatus(i.deviceInfo),i.$nextTick((function(){this.MonitorChart()}))})),this.connectMqtt())}},data:function(){return{title:"设备控制 ",shadowUnEnable:!1,statusColor:{background:"#67C23A",color:"#fff"},firmware:{},openFirmware:!1,loading:!0,deviceInfo:{boolList:[],enumList:[],stringList:[],integerList:[],decimalList:[],arrayList:[],readOnlyList:[]},monitorChart:[{chart:{},data:{id:"",name:"",value:""}}]}},created:function(){},methods:{connectMqtt:function(){var e=this;return Object(r["a"])(Object(a["a"])().mark((function t(){return Object(a["a"])().wrap((function(t){while(1)switch(t.prev=t.next){case 0:if(null!=e.$mqttTool.client){t.next=3;break}return t.next=3,e.$mqttTool.connect(e.vuex_token);case 3:e.mqttCallback();case 4:case"end":return t.stop()}}),t)})))()},mqttCallback:function(){var e=this;this.$mqttTool.client.on("message",(function(t,i,n){var o=t.split("/"),a=(o[1],o[2]);if(i=JSON.parse(i.toString()),"status"==o[3]&&(console.log("接收到【设备状态-运行】主题：",t),console.log("接收到【设备状态-运行】内容：",i),e.deviceInfo.serialNumber==a&&(e.deviceInfo.status=i.status,e.deviceInfo.isShadow=i.isShadow,e.deviceInfo.rssi=i.rssi,e.updateDeviceStatus(e.deviceInfo))),("property"==o[3]||"function"==o[3])&&(console.log("接收到【物模型】主题：",t),console.log("接收到【物模型】内容：",i),e.deviceInfo.serialNumber==a))for(var r=0;r<i.length;r++){for(var s=!1,l=0;l<e.deviceInfo.boolList.length&&!s;l++)if(e.deviceInfo.boolList[l].id==i[r].id){e.deviceInfo.boolList[l].shadow=i[r].value,s=!0;break}for(var c=0;c<e.deviceInfo.enumList.length&&!s;c++)if(e.deviceInfo.enumList[c].id==i[r].id){e.deviceInfo.enumList[c].shadow=i[r].value,s=!0;break}for(var d=0;d<e.deviceInfo.stringList.length&&!s;d++)if(e.deviceInfo.stringList[d].id==i[r].id){e.deviceInfo.stringList[d].shadow=i[r].value,s=!0;break}for(var u=0;u<e.deviceInfo.arrayList.length&&!s;u++)if(e.deviceInfo.arrayList[u].id==i[r].id){e.deviceInfo.arrayList[u].shadow=i[r].value,s=!0;break}for(var f=0;f<e.deviceInfo.integerList.length&&!s;f++)if(e.deviceInfo.integerList[f].id==i[r].id){e.deviceInfo.integerList[f].shadow=i[r].value,s=!0;break}for(var m=0;m<e.deviceInfo.decimalList.length&&!s;m++)if(e.deviceInfo.decimalList[m].id==i[r].id){e.deviceInfo.decimalList[m].shadow=i[r].value,s=!0;break}for(var v=0;v<e.deviceInfo.readOnlyList.length&&!s;v++)if(e.deviceInfo.readOnlyList[v].id==i[r].id){e.deviceInfo.readOnlyList[v].shadow=i[r].value;for(var p=0;p<e.monitorChart.length;p++)if(i[r].id==e.monitorChart[p].data.id){var h=[{value:i[r].value,name:e.monitorChart[p].data.name}];e.monitorChart[p].chart.setOption({series:[{data:h}]});break}s=!0;break}}}))},publishThingsModel:function(e,t){var i=this;Object(c["b"])(e.productId).then((function(n){for(var o=JSON.parse(n.data),a=0,r=0;r<o.functions.length;r++)if(t.id==o.functions[r].id){a=2;break}if(0==a)for(var s=0;s<o.properties.length;s++)if(t.id==o.properties[s].id){a=1;break}0!=a&&i.mqttPublish(a,e,t)}))},mqttPublish:function(e,t,i){var n=this,o="",a="";if(1==e)3==t.status?o="/"+t.productId+"/"+t.serialNumber+"/property-online/get":t.isShadow&&(o="/"+t.productId+"/"+t.serialNumber+"/property-offline/post"),a='[{"id":"'+i.id+'","value":"'+i.shadow+'"}]';else if(2==e)3==t.status?o="/"+t.productId+"/"+t.serialNumber+"/function-online/get":t.isShadow&&(o="/"+t.productId+"/"+t.serialNumber+"/function-offline/post"),a='[{"id":"'+i.id+'","value":"'+i.shadow+'"}]';else{if(3!=e)return;o="/"+t.productId+"/"+t.serialNumber+"/ota/get",a='{"version":'+this.firmware.version+',"downloadUrl":"'+this.getDownloadUrl(this.firmware.filePath)+'"}'}""!=o&&this.$mqttTool.publish(o,a,i.name).then((function(e){n.$modal.notifySuccess(e)})).catch((function(e){n.$modal.notifyError(e)}))},updateDeviceStatus:function(e){3==e.status?(this.statusColor.background="#12d09f",this.title="在线模式"):1==e.isShadow?(this.statusColor.background="#409EFF",this.title="影子模式"):(this.statusColor.background="#909399",this.title="离线模式",this.shadowUnEnable=!0),this.$emit("statusEvent",this.deviceInfo.status)},otaUpgrade:function(){var e={name:"设备升级"};this.mqttPublish(3,this.deviceInfo,e),this.openFirmware=!1},getLatestFirmware:function(e){var t=this;Object(l["d"])(e).then((function(e){t.firmware=e.data,t.openFirmware=!0}))},cancel:function(){this.openFirmware=!1},getDownloadUrl:function(e){return window.location.origin+"/prod-api"+e},MonitorChart:function(){for(var e=0;e<this.deviceInfo.readOnlyList.length;e++){var t;this.monitorChart[e]={chart:d["init"](this.$refs.map[e]),data:{id:this.deviceInfo.readOnlyList[e].id,name:this.deviceInfo.readOnlyList[e].name,value:this.deviceInfo.readOnlyList[e].shadow}},t={tooltip:{formatter:" {b} <br/> {c}"+this.deviceInfo.readOnlyList[e].unit},series:[{name:this.deviceInfo.readOnlyList[e].type,type:"gauge",min:this.deviceInfo.readOnlyList[e].min,max:this.deviceInfo.readOnlyList[e].max,colorBy:"data",splitNumber:10,radius:"100%",splitLine:{distance:4},axisLabel:{fontSize:10,distance:10},axisTick:{distance:4},axisLine:{lineStyle:{width:8,color:[[.2,"#409EFF"],[.8,"#12d09f"],[1,"#F56C6C"]],opacity:.3}},pointer:{icon:"triangle",length:"60%",width:7},progress:{show:!0,width:8},detail:{valueAnimation:!0,formatter:"{value} "+this.deviceInfo.readOnlyList[e].unit,offsetCenter:[0,"80%"],fontSize:20},data:[{value:this.deviceInfo.readOnlyList[e].shadow,name:this.deviceInfo.readOnlyList[e].name}],title:{offsetCenter:[0,"115%"],fontSize:16}}]},t&&this.monitorChart[e].chart.setOption(t)}}}},f=u,m=i("2877"),v=Object(m["a"])(f,n,o,!1,null,null,null);t["default"]=v.exports},"814a":function(e,t,i){"use strict";i.d(t,"e",(function(){return o})),i.d(t,"d",(function(){return a})),i.d(t,"c",(function(){return r})),i.d(t,"a",(function(){return s})),i.d(t,"f",(function(){return l})),i.d(t,"b",(function(){return c}));var n=i("b775");function o(e){return Object(n["a"])({url:"/iot/firmware/list",method:"get",params:e})}function a(e){return Object(n["a"])({url:"/iot/firmware/getLatest/"+e,method:"get"})}function r(e){return Object(n["a"])({url:"/iot/firmware/"+e,method:"get"})}function s(e){return Object(n["a"])({url:"/iot/firmware",method:"post",data:e})}function l(e){return Object(n["a"])({url:"/iot/firmware",method:"put",data:e})}function c(e){return Object(n["a"])({url:"/iot/firmware/"+e,method:"delete"})}}}]);