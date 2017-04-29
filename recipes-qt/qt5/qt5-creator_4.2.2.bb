SUMMARY = "Qt Creator is a new cross-platform Qt IDE"

# Note:
# The toolchain auto detection does not work completely yet. To compile/debug
# open menu 'Tools/Options and select 'Build & Run'. In tab 'Kits' select 'Desktop'
# 'Compiler/Manage...' and add local gcc'. At 'Debugger' select
# 'System GDB at /usr/bin/gdb.

HOMEPAGE = "https://qt-project.org/"
LICENSE = "GPLv3 & The-Qt-Company-GPL-Exception-1.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE.GPL3-EXCEPT;md5=763d8c535a234d9a3fb682c7ecb6c073 \
"

inherit qmake5

DEPENDS = "qtbase qtscript qtwebkit qtwebengine qtxmlpatterns qtx11extras qtdeclarative qttools qttools-native qtsvg qtquick1"

SRC_URI = " \
    http://download.qt.io/official_releases/qtcreator/4.2/${PV}/qt-creator-opensource-src-${PV}.tar.gz \
    file://0001-Fix-Allow-qt-creator-to-build-on-arm-aarch32-and-aar.patch \
    file://qtcreator.desktop.in \
"
SRC_URI[md5sum] = "c5dad9aa00021112cc12578622c1c1c1"
SRC_URI[sha256sum] = "0402f0e33bf3c8ee7f89c673f25fcf75fef03ba4262e077d9d9f42454d1e799b"

S = "${WORKDIR}/qt-creator-opensource-src-${PV}"

EXTRA_QMAKEVARS_PRE += "IDE_LIBRARY_BASENAME=${baselib}${QT_DIR_NAME}"

do_configure_append() {
    # Find native tools
    sed -i 's:${STAGING_BINDIR}.*/lrelease:${OE_QMAKE_PATH_EXTERNAL_HOST_BINS}/lrelease:g' ${B}/share/qtcreator/translations/Makefile
    sed -i 's:${STAGING_BINDIR}.*/qdoc:${OE_QMAKE_PATH_EXTERNAL_HOST_BINS}/qdoc:g' ${B}/Makefile

    # see qtbase-native.inc
    # sed -i 's:QT_INSTALL_DOCS=${docdir}:QT_INSTALL_DOCS=${STAGING_DATADIR_NATIVE}${QT_DIR_NAME}/doc:g' ${B}/Makefile
}

do_compile_append() {
    # build docs
    #oe_runmake docs_online
}

do_install() {
    oe_runmake install INSTALL_ROOT=${D}${prefix}
    oe_runmake install_inst_qch_docs INSTALL_ROOT=${D}${prefix}
}

FILES_${PN} += " \
    ${datadir}/qtcreator \
    ${datadir}/icons \
    ${datadir}/metainfo \
    ${libdir}${QT_DIR_NAME}/qtcreator \
"
FILES_${PN}-dbg += " \
    ${libdir}${QT_DIR_NAME}/qtcreator/.debug \
    ${libdir}${QT_DIR_NAME}/qtcreator/plugins/.debug \
    ${libdir}${QT_DIR_NAME}/qtcreator/plugins/qmldesigner/.debug \
    ${libdir}${QT_DIR_NAME}/qtcreator/plugins/qbs/plugins/.debug \
"

FILES_${PN}-dev += " \
    ${libdir}${QT_DIR_NAME}/qtcreator/*${SOLIBSDEV} \
"

RDEPENDS_${PN} += " \
    perl \
    bash \
"
RCONFLICTS_${PN} = "qt-creator"

# To give best user experience out of the box..
RRECOMMENDS_${PN} += " \
    packagegroup-qt5-toolchain-target \
    binutils \
    ccache \
    make \
    gcc-symlinks g++-symlinks cpp-symlinks \
    gdb \
"
