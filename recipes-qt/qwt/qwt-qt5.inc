DESCRIPTION = "Qt Widget Extension for Technical Applications"
SECTION = "libs"

# LGPLv2.1 + some exceptions
LICENSE = "QWTv1.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=dac2743472b0462ff3cfb4af42051c88"

DEPENDS += "qtbase qtsvg"

SRC_URI = "${SOURCEFORGE_MIRROR}/qwt/qwt-${PV}.tar.bz2;name=qwt"

S = "${WORKDIR}/qwt-${PV}"

do_configure_prepend() {
    sed -e 's/QWT_CONFIG     += QwtDesigner/#QWT_CONFIG     += QwtDesigner/g' -i ${S}/qwtconfig.pri
    sed -i -e 's:/usr/local/qwt-$$QWT_VERSION:${prefix}:g' ${S}/*.pri
}

PACKAGES_prepend = "${PN}-examples ${PN}-features ${PN}-plugins "
FILES_${PN}-examples = "${bindir}/*"
FILES_${PN}-features = "${prefix}/features"
FILES_${PN}-plugins = "${OE_QMAKE_PATH_PLUGINS}/designer/*.so"
FILES_${PN}-dbg += "${OE_QMAKE_PATH_PLUGINS}/designer/.debug"
FILES_${PN}-doc += "${prefix}/doc"
