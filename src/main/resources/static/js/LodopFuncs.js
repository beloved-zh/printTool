const install_lodop32 = 'install_lodop32.exe'
const install_lodop64 = 'install_lodop64.exe'
const CLodop_Setup_for_Win32NT = 'CLodop_Setup_for_Win32NT.exe'
var CreatedOKLodopObject, CLodopIsLocal, CLodopJsState;

// ==判断是否需要CLodop(那些不支持插件的浏览器):==
function needCLodop() {
    try {
        const ua = navigator.userAgent
        if (ua.match(/Windows\sPhone/i)) return true
        if (ua.match(/iPhone|iPod|iPad/i)) return true
        if (ua.match(/Android/i)) return true
        if (ua.match(/Edge\D?\d+/i)) return true

        const verTrident = ua.match(/Trident\D?\d+/i)
        const verIE = ua.match(/MSIE\D?\d+/i)
        let verOPR = ua.match(/OPR\D?\d+/i)
        let verFF = ua.match(/Firefox\D?\d+/i)
        const x64 = ua.match(/x64/i)
        if (!verTrident && !verIE && x64) return true
        else if (verFF) {
            verFF = verFF[0].match(/\d+/)
            if (verFF[0] >= 41 || x64) return true
        } else if (verOPR) {
            verOPR = verOPR[0].match(/\d+/)
            if (verOPR[0] >= 32) return true
        } else if (!verTrident && !verIE) {
            let verChrome = ua.match(/Chrome\D?\d+/i)
            if (verChrome) {
                verChrome = verChrome[0].match(/\d+/)
                if (verChrome[0] >= 41) return true
            }
        }
        return false
    } catch (err) {
        return true
    }
}

// ==加载引用CLodop的主JS,用双端口8000和18000(以防其中一个被占):==
function loadCLodop() {
    if (CLodopJsState === 'loading' || CLodopJsState === 'complete') return
    CLodopJsState = 'loading'
    const head = document.head || document.getElementsByTagName('head')[0] || document.documentElement
    const JS1 = document.createElement('script')
    const JS2 = document.createElement('script')
    JS1.src = 'http://localhost:8000/CLodopfuncs.js?priority=1'
    JS2.src = 'http://localhost:18000/CLodopfuncs.js'
    JS1.onload = JS2.onload = function() {
        CLodopJsState = 'complete'
    }
    JS1.onerror = JS2.onerror = function() {
        CLodopJsState = 'complete'
    }
    head.insertBefore(JS1, head.firstChild)
    head.insertBefore(JS2, head.firstChild)
    CLodopIsLocal = !!(JS1.src + JS2.src).match(/\/\/localho|\/\/127.0.0./i)
}

//开始加载
if (needCLodop()) {
    loadCLodop()
}

//==获取LODOP对象主过程,判断是否安装、需否升级:==
function getLodop(oOBJECT, oEMBED) {
    var strLodopInstall = "<br><font color='#FF00FF'>打印控件未安装!点击这里<a href='" + install_lodop32 + "' target='_self'>执行安装</a>，成功后请刷新本页面或重启浏览器。</font>"
    var strLodop64Install = "<br><font color='#FF00FF'>打印控件未安装!点击这里<a href='" + install_lodop64 + "' target='_self'>执行安装</a>，成功后请刷新本页面或重启浏览器。</font>"
    var strLodopUpdate = "<br><font color='#FF00FF'>打印控件需要升级!点击这里<a href='" + install_lodop32 + "' target='_self'>执行升级</a>，成功后请刷新本页面或重启浏览器。</font>"
    var strLodop64Update = "<br><font color='#FF00FF'>打印控件需要升级!点击这里<a href='" + install_lodop64 + "' target='_self'>执行升级</a>，成功后请刷新本页面或重启浏览器。</font>"
    var strCLodopInstall = "<br><font color='#FF00FF'>Web打印服务CLodop未安装，点击这里<a href='" + CLodop_Setup_for_Win32NT + "' target='_self'>下载执行安装</a>，成功后请刷新本页面或重启浏览器。</font>"
    var strCLodopUpdate = "<br><font color='#FF00FF'>Web打印服务CLodop需要升级，点击这里<a href='" + CLodop_Setup_for_Win32NT + "' target='_self'>执行升级</a>，成功后请刷新本页面或重启浏览器。</font>"
    var strCLodopStart = "<br><font color='#FF00FF'>（若此前已安装过，可<a href='CLodop.protocol:setup' target='_self'>点这里直接再次启动</a>），成功后请刷新本页面或重启浏览器。</font>"
    
    var LODOP;
    try {
        var isIE    = (/MSIE/i.test(navigator.userAgent)) || (/Trident/i.test(navigator.userAgent));
        var isX64  = (/x64/i.test(navigator.userAgent));
        
        if (needCLodop()) {
            try {
                LODOP = getCLodop();
            } catch (err) {
                console.error(err)
            }

            if (!LODOP && CLodopJsState !== "complete") {
                if (CLodopJsState == "loading") {
                    alert("网页还没下载完毕，请稍等一下再操作.");
                } else {
                    alert("未曾加载Lodop主JS文件，请先调用loadCLodop过程.");
                }
                return;
            }
            
            var strAlertMessage;
            if (!LODOP) {
                if (!CLodopIsLocal) {
                    strAlertMessage = strCLodopInstall
                } else {
                    strAlertMessage = strCLodopInstall + strCLodopStart
                }
                document.body.innerHTML = strAlertMessage + document.body.innerHTML;
                return;
            } else {
                if (LODOP.CVERSION < '4.1.5.8') {
                    strAlertMessage = strCLodopUpdate
                }
                if (strAlertMessage) {
                    document.body.innerHTML = strAlertMessage + document.body.innerHTML;
                    return;
                }
            }
        } else {
            //==如果页面有Lodop插件就直接使用,否则新建:==
            if (oOBJECT || oEMBED) {
                if (isIE) {
                    LODOP = oOBJECT;
                } else {
                    LODOP = oEMBED;
                }
            } else if (!CreatedOKLodopObject) {
                LODOP = document.createElement("object");
                LODOP.setAttribute("width", 100);
                LODOP.setAttribute("height", 100);
                LODOP.setAttribute("style", "position:absolute;left:0px;top:-100px;width:0px;height:0px;");
                if (isIE) {
                    LODOP.setAttribute("classid", "clsid:2105C259-1E0C-4534-8141-A753534CB4CA");
                } else {
                    LODOP.setAttribute("type", "application/x-print-lodop");
                }
                document.documentElement.appendChild(LODOP);
                CreatedOKLodopObject = LODOP;
            } else {
                LODOP = CreatedOKLodopObject;
            }
            
            //== Lodop插件未安装时提示下载地址:==
            if ((!LODOP) || (!LODOP.VERSION)) {
                document.body.innerHTML = (isX64 ? strLodop64Install : strLodopInstall) + document.body.innerHTML;
                return LODOP;
            }
            if (LODOP.VERSION < "6.2.2.6") {
                document.body.innerHTML = (isX64 ? strLodop64Update : strLodopUpdate) + document.body.innerHTML;
            }
        }
        //===如下空白位置适合调用统一功能(如注册语句、语言选择等):=======================
        LODOP.SET_LICENSES('xxxxx公司', '4AA5CA8E56EDABDCD8F007D7B4FB9152922', '', '')

        //===============================================================================
        return LODOP;
    } catch (err) {
        alert("getLodop出错:" + err);
    }
}
