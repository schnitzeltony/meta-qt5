SUMMARY = "Python Qt5 Bindings"
AUTHOR = "Phil Thomson @ riverbank.co.uk"
HOMEPAGE = "http://riverbankcomputing.co.uk"
SECTION = "devel/python"
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "\
    file://LICENSE;md5=d32239bcb673463ab874e80d47fae504 \
"
SRC_URI = "\
    ${SOURCEFORGE_MIRROR}/pyqt/PyQt5_gpl-${PV}.tar.gz \
"
SRC_URI[md5sum] = "aac3428e90eca5df68c5764282fd9b98"
SRC_URI[sha256sum] = "c9b57d15601d436faf35dacf8e0acefa220194829a653e771e80b189b3261073"

S = "${WORKDIR}/PyQt5_gpl-${PV}"

BBCLASSEXTEND = "native"

inherit qmake5

DEPENDS = "qtbase"
DEPENDS_append_class-target = " \
    ${@bb.utils.filter('PACKAGECONFIG', 'qtlocation', d)} \
    ${@bb.utils.filter('PACKAGECONFIG', 'qtmultimedia', d)} \
    ${@bb.utils.filter('PACKAGECONFIG', 'qtdeclarative', d)} \
    ${@bb.utils.filter('PACKAGECONFIG', 'qtsensors', d)} \
    ${@bb.utils.filter('PACKAGECONFIG', 'qtserialport', d)} \
    ${@bb.utils.filter('PACKAGECONFIG', 'qtsvg', d)} \
    ${@bb.utils.filter('PACKAGECONFIG', 'qtwebchannel', d)} \
    ${@bb.utils.filter('PACKAGECONFIG', 'qtwebengine', d)} \
    ${@bb.utils.filter('PACKAGECONFIG', 'qtwebkit', d)} \
    ${@bb.utils.filter('PACKAGECONFIG', 'qtwebsockets', d)} \
    ${@bb.utils.filter('PACKAGECONFIG', 'qtx11extras', d)} \
    ${@bb.utils.filter('PACKAGECONFIG', 'qtxmlpatterns', d)} \
"

B = "${S}"

export BUILD_SYS
export HOST_SYS

PARALLEL_MAKEINST = ""

# QtCore disabled features
DISABLED_FEATURES ??= " PyQt_Accessibility PyQt_SessionManager"
DISABLED_FEATURES_ARCH = ""
DISABLED_FEATURES_ARCH_arm = "PyQt_qreal_double"
DISABLED_FEATURES_GL = "${@bb.utils.contains('PACKAGECONFIG', 'gles2', 'PyQt_Desktop_OpenGL', '', d)}"
DISABLED_FEATURES_append = " ${DISABLED_FEATURES_ARCH} ${DISABLED_FEATURES_GL}"

PYQT_MODULES = "QtCore"
# Notes on PACKAGECONFIGs / PYQT_MODULES:
# * print: Does not work currently 'fatal error: qprinter.h' -> PACKAGECONFIG for qtbase?
# * qtwebkit: cannot add QtWebKitWidgets for same 'fatal error: qprinter.h'
# * qtwebengine: cannot add QtWebEngineWidgets for same 'fatal error: qprinter.h'
PYQT_MODULES_append_class-target = " \
    QtDBus QtGui QtNetwork QtWidgets QtXml \
    ${@bb.utils.contains_any('PACKAGECONFIG', ['gl', 'gles2'], 'QtOpenGL', '', d)} \
    \
    ${@bb.utils.contains('PACKAGECONFIG', 'sql', 'QtSql', '', d)} \
    ${@bb.utils.contains('PACKAGECONFIG', 'print', 'QtPrintSupport', '', d)} \
    \
    ${@bb.utils.contains('PACKAGECONFIG', 'qtlocation', 'QtLocation', '', d)} \
    ${@bb.utils.contains('PACKAGECONFIG', 'qtmultimedia', 'QtMultimedia QtMultimediaWidgets', '', d)} \
    ${@bb.utils.contains('PACKAGECONFIG', 'qtdeclarative', 'QtQml QtQuick QtQuickWidgets', '', d)} \
    ${@bb.utils.contains('PACKAGECONFIG', 'qtsensors', 'QtSensors', '', d)} \
    ${@bb.utils.contains('PACKAGECONFIG', 'qtserialport', 'QtSerialPort', '', d)} \
    ${@bb.utils.contains('PACKAGECONFIG', 'qtsvg', 'QtSvg', '', d)} \
    ${@bb.utils.contains('PACKAGECONFIG', 'qtwebchannel', 'QtWebChannel', '', d)} \
    ${@bb.utils.contains('PACKAGECONFIG', 'qtwebengine', 'QtWebEngine QtWebEngineCore', '', d)} \
    ${@bb.utils.contains('PACKAGECONFIG', 'qtwebkit', 'QtWebKit', '', d)} \
    ${@bb.utils.contains('PACKAGECONFIG', 'qtwebsockets', 'QtWebSockets', '', d)} \
    ${@bb.utils.contains('PACKAGECONFIG', 'qtx11extras', 'QtX11Extras', '', d)} \
    ${@bb.utils.contains('PACKAGECONFIG', 'qtxmlpatterns', 'QtXmlPatterns', '', d)} \
"

# QtBase GL: Make sure to keep GL/GLES packageconfigs in sync with qtbase!!!
PACKAGECONFIG_GL ?= "${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'gl', '', d)}"
PACKAGECONFIG[gl] = ""
PACKAGECONFIG[gles2] = ""
PACKAGECONFIG[no-opengl] = ""

# QtBase misc
PACKAGECONFIG[sql] = ""
PACKAGECONFIG[print] = ""

# Other Qt modules
PACKAGECONFIG[qtlocation] = ""
PACKAGECONFIG[qtmultimedia] = ""
PACKAGECONFIG[qtdeclarative] = ""
PACKAGECONFIG[qtsensors] = ""
PACKAGECONFIG[qtserialport] = ""
PACKAGECONFIG[qtsvg] = ""
PACKAGECONFIG[qtwebchannel] = ""
PACKAGECONFIG[qtwebengine] = ""
PACKAGECONFIG[qtwebkit] = ""
PACKAGECONFIG[qtwebsockets] = ""
PACKAGECONFIG[qtx11extras] = ""
PACKAGECONFIG[qtxmlpatterns] = ""

PACKAGECONFIG ?= " \
    ${PACKAGECONFIG_GL} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'qtx11extras', '', d)} \
"

SYSROOTDIR = "${STAGING_DIR_HOST}"
SYSROOTDIR_class-native = "${STAGING_DIR_NATIVE}"
INCLUDEDIR = "${STAGING_INCDIR}"
INCLUDEDIR_class-native = "${STAGING_INCDIR_NATIVE}"
PYLIBDIR = "${STAGING_DIR_HOST}/${PYTHON_SITEPACKAGES_DIR}"
PYLIBDIR_class-native = "${STAGING_DIR_NATIVE}/${PYTHON_SITEPACKAGES_DIR}"
PYTHONEXEC="${bindir}/${PYTHON_PN}"
PYTHONEXEC_class-native="${PYTHON}"
SIPDIR="${datadir}/sip/PyQt5"

do_configure_prepend() {
    echo "py_platform = linux" > pyqt.cfg
    echo "py_inc_dir = ${INCLUDEDIR}/python${PYTHON_BASEVERSION}${PYTHON_ABI}" >> pyqt.cfg
    echo "py_pylib_dir = ${PYLIBDIR}" >> pyqt.cfg
    echo "py_pylib_lib = python${PYTHON_BASEVERSION}${PYTHON_ABI}" >> pyqt.cfg
    echo "pyqt_module_dir = ${D}${PYTHON_SITEPACKAGES_DIR}" >> pyqt.cfg
    echo "pyqt_bin_dir = ${D}${bindir}" >> pyqt.cfg
    echo "pyqt_sip_dir = ${D}${SIPDIR}" >> pyqt.cfg
    echo "pyuic_interpreter = ${PYTHONEXEC}" >> pyqt.cfg
    if [ ! -z `echo "${DISABLED_FEATURES}" | sed 's: ::g'` ]; then
        echo "pyqt_disabled_features = ${DISABLED_FEATURES}" >> pyqt.cfg
    fi
    echo "qt_shared = True" >> pyqt.cfg
    QT_VERSION=`${OE_QMAKE_QMAKE} -query QT_VERSION`
    echo "[Qt $QT_VERSION]" >> pyqt.cfg
    echo "pyqt_modules = ${PYQT_MODULES}" >> pyqt.cfg
    echo yes | ${PYTHON} configure.py --verbose --qmake ${OE_QMAKE_QMAKE} --configuration pyqt.cfg --sysroot ${SYSROOTDIR}
}

do_compile() {
    oe_runmake
}

do_install() {
    oe_runmake MAKEFLAGS='-j 1' install
}

do_install_class-native() {
    oe_runmake MAKEFLAGS='-j 1' install

    # Avoid getting build based on sip files for qt-native
    rm -rf ${D}${SIPDIR}

    # no absolute path to build sysroot in scripts
    for script in `find "${D}${bindir}" -name 'py*5'`; do
        sed -i 's:${PYTHON}:${'PYTHON'}:g' $script
    done
}

FILES_${PN} += "${PYTHON_SITEPACKAGES_DIR}"

FILES_${PN}-dev += " \
    ${bindir}/py*5 \
    ${SIPDIR} \
"
