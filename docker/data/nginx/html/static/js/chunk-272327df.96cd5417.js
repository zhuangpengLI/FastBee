(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-272327df","chunk-2d0b9594"],{"15c9":function(t,e,i){"use strict";i.r(e);var s=function(){var t=this,e=t.$createElement,i=t._self._c||e;return i("div",{ref:"appRef",staticClass:"index_home",class:{pageisScale:!0},attrs:{id:"index"}},[i("div",{staticClass:"bg"},[t.loading?i("dv-loading",[t._v("Loading...")]):i("div",{staticClass:"host-body"},[i("div",{staticClass:"d-flex jc-center title_wrap"},[i("div",{staticClass:"zuojuxing"}),i("div",{staticClass:"youjuxing"}),i("div",{staticClass:"guang"}),i("div",{staticClass:"d-flex jc-center"},[i("div",{staticClass:"title"},[i("span",{staticClass:"title-text"},[t._v("FastBee物联网平台")])])]),i("div",{staticClass:"timers "},[t._v(" "+t._s(t.dateYear)+" "+t._s(t.dateWeek)+" "+t._s(t.dateDay)+" "),i("i",{staticClass:" blq-icon-shezhi02",staticStyle:{"margin-left":"10px"},on:{click:t.showSetting}})])]),i("index")],1)],1),i("Setting",{ref:"setting"})],1)},n=[],a=(i("b680"),i("99af"),{width:"1",height:"1"}),o=1920,l=1080,c=parseFloat((o/l).toFixed(5)),r={data:function(){return{drawTiming:null}},computed:{isScale:function(){return this.$store.state.settings.isScale}},mounted:function(){this.isScale&&(this.calcRate(),window.addEventListener("resize",this.resize))},beforeDestroy:function(){window.removeEventListener("resize",this.resize)},methods:{calcRate:function(){var t=this.$refs["appRef"];if(t){var e=parseFloat((window.innerWidth/window.innerHeight).toFixed(5));t&&(e>c?(a.width=(window.innerHeight*c/o).toFixed(5),a.height=(window.innerHeight/l).toFixed(5),t.style.transform="scale(".concat(a.width,", ").concat(a.height,") translate(-50%, -50%)")):(a.height=(window.innerWidth/c/l).toFixed(5),a.width=(window.innerWidth/o).toFixed(5),t.style.transform="scale(".concat(a.width,", ").concat(a.height,") translate(-50%, -50%)")))}},resize:function(){var t=this;this.isScale&&(clearTimeout(this.drawTiming),this.drawTiming=setTimeout((function(){t.calcRate()}),200))}}};i("ac1f"),i("00b4"),i("5319"),i("4d63"),i("c607"),i("2c3e"),i("25f0");function d(t,e){if(t){var i=new Date(t),s={"M+":i.getMonth()+1,"d+":i.getDate(),"H+":i.getHours(),"m+":i.getMinutes(),"s+":i.getSeconds(),"q+":Math.floor((i.getMonth()+3)/3),S:i.getMilliseconds()};for(var n in/(y+)/.test(e)&&(e=e.replace(RegExp.$1,(i.getFullYear()+"").substr(4-RegExp.$1.length))),s)new RegExp("("+n+")").test(e)&&(e=e.replace(RegExp.$1,1===RegExp.$1.length?s[n]:("00"+s[n]).substr((""+s[n]).length)));return e}return""}var u=i("3320"),g=i("bbfc"),h={mixins:[r],components:{Setting:u["default"],index:g["default"]},data:function(){return{timing:null,loading:!0,dateDay:null,dateYear:null,dateWeek:null,weekday:["周日","周一","周二","周三","周四","周五","周六"]}},filters:{numsFilter:function(t){return t||0}},computed:{},created:function(){},mounted:function(){this.timeFn(),this.cancelLoading()},beforeDestroy:function(){clearInterval(this.timing)},methods:{showSetting:function(){this.$refs.setting.init()},timeFn:function(){var t=this;this.timing=setInterval((function(){t.dateDay=d(new Date,"HH: mm: ss"),t.dateYear=d(new Date,"yyyy-MM-dd"),t.dateWeek=t.weekday[(new Date).getDay()]}),1e3)},cancelLoading:function(){var t=this;setTimeout((function(){t.loading=!1}),500)}}},f=h,v=(i("ee65"),i("2877")),w=Object(v["a"])(f,s,n,!1,null,null,null);e["default"]=w.exports},3320:function(t,e,i){"use strict";i.r(e);var s=function(){var t=this,e=t.$createElement,i=t._self._c||e;return i("transition",{attrs:{name:"yh-setting-fade"}},[i("div",{directives:[{name:"show",rawName:"v-show",value:t.settingShow,expression:"settingShow"}],staticClass:"setting",class:{settingShow:t.settingShow}},[i("div",{staticClass:"setting_dislog",on:{click:function(e){t.settingShow=!1}}}),i("div",{staticClass:"setting_inner"},[i("div",{staticClass:"setting_header"},[t._v(" 设置 ")]),i("div",{staticClass:"setting_body"},[i("div",{staticClass:"left_shu"},[t._v(" 全局设置")]),i("div",{staticClass:"setting_item"},[i("span",{staticClass:"setting_label"},[t._v(" 是否进行自动适配"),i("span",{staticClass:"setting_label_tip"},[t._v("(默认分辨率1920*1080)")]),t._v(": ")]),i("div",{staticClass:"setting_content"},[i("el-radio-group",{on:{change:function(e){return t.radiochange(e,"isScale")}},model:{value:t.isScaleradio,callback:function(e){t.isScaleradio=e},expression:"isScaleradio"}},[i("el-radio",{attrs:{label:!0}},[t._v("是")]),i("el-radio",{attrs:{label:!1}},[t._v("否")])],1)],1)]),i("div",{staticClass:"left_shu"},[t._v(" 实时监测")]),i("div",{staticClass:"setting_item"},[i("span",{staticClass:"setting_label"},[t._v(" 设备提醒自动轮询: "),i("span",{staticClass:"setting_label_tip"})]),i("div",{staticClass:"setting_content"},[i("el-radio-group",{on:{change:function(e){return t.radiochange(e,"sbtxSwiper")}},model:{value:t.sbtxradio,callback:function(e){t.sbtxradio=e},expression:"sbtxradio"}},[i("el-radio",{attrs:{label:!0}},[t._v("是")]),i("el-radio",{attrs:{label:!1}},[t._v("否")])],1)],1)]),i("div",{staticClass:"setting_item"},[i("span",{staticClass:"setting_label"},[t._v(" 实时预警轮播: ")]),i("div",{staticClass:"setting_content"},[i("el-radio-group",{on:{change:function(e){return t.radiochange(e,"ssyjSwiper")}},model:{value:t.ssyjradio,callback:function(e){t.ssyjradio=e},expression:"ssyjradio"}},[i("el-radio",{attrs:{label:!0}},[t._v("是")]),i("el-radio",{attrs:{label:!1}},[t._v("否")])],1)],1)]),i("div",{staticClass:"flex justify-center"})])])])])},n=[],a={components:{},data:function(){return{settingShow:!1,sbtxradio:!0,ssyjradio:!0,isScaleradio:!0}},computed:{},methods:{init:function(){this.settingShow=!0},radiochange:function(t,e){this.$store.commit("setting/updateSwiper",{val:t,type:e}),"isScale"===e&&this.$router.go(0)}},created:function(){},mounted:function(){document.body.appendChild(this.$el)},beforeDestroy:function(){},destroyed:function(){this.$el&&this.$el.parentNode&&this.$el.parentNode.removeChild(this.$el)}},o=a,l=i("2877"),c=Object(l["a"])(o,s,n,!1,null,"1fcee248",null);e["default"]=c.exports},ee65:function(t,e,i){"use strict";i("f245")},f245:function(t,e,i){}}]);