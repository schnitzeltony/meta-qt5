DESCRIPTION = "Plugins for a virtual keyboard for touch-screen based user interfaces"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=f29b21caa8e460097bfad9c026a33621"

inherit qmake5

DEPENDS = "maliit-framework-qt5"

RDEPENDS_${PN} += "qtsvg-plugins"

SRC_URI = "git://github.com/maliit/plugins.git"

SRCREV = "3c7b94d940c2013480fb9848f1be277bdf24bf3d"
PV = "0.99.0+git${SRCPV}"

EXTRA_QMAKEVARS_PRE = "\
    CONFIG+=nodoc \
    CONFIG+=notests \
"

FILES_${PN} += "\
    ${libdir}/maliit \
    ${datadir} \
"

FILES_${PN}-dbg += "${libdir}/maliit/plugins/.debug"

S= "${WORKDIR}/git"
