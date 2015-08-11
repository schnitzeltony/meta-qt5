DESCRIPTION = "The GTK+ input context for Maliit"

LICENSE = "LGPLv2"
LIC_FILES_CHKSUM = "file://LICENSE.LGPL;md5=5c917f6ce94ceb8d8d5e16e2fca5b9ad"

inherit qmake5 pkgconfig gtk-immodules-cache

DEPENDS = "maliit-framework-qt5"

SRC_URI = " \
    git://github.com/maliit/inputcontext-gtk.git \
    file://0001-make-build-for-gtk2-gtk3-immodules-configurable.patch \
"
SRCREV = "50f2fb2f1ec932d6ec3bc4b217724c93b5f7bae1"
PV = "0.99.0+git${SRCPV}"

S= "${WORKDIR}/git"

EXTRA_QMAKEVARS_PRE = "\
    CONFIG+=disable-gtk-cache-update \
    CONFIG+=notests \
    ${@base_contains('PACKAGECONFIG', 'gtk2', 'CONFIG+=gtk2', '', d)} \
    ${@base_contains('PACKAGECONFIG', 'gtk3', 'CONFIG+=gtk3', '', d)} \
"

PACKAGECONFIG ??= "gtk2 gtk3"
PACKAGECONFIG[gtk2] = ",,gtk+"
PACKAGECONFIG[gtk3] = ",,gtk+3"

FILES_${PN} += "${libdir}/gtk-*"
FILES_${PN}-dbg += "${libdir}/*/*/immodules/.debug"

pkg_postinst_${PN} () {
#!/bin/sh
# should run online
if [ "x$D" != "x" ]; then
    exit 1
fi
echo "export GTK_IM_MODULE=Maliit" >> /etc/xprofile
}

pkg_postrm_${PN} () {
#!/bin/sh
# should run online
if [ "x$D" = "x" ]; then
    exit 1
fi
if [ -e "/etc/xprofile" ]; then
    sed -i -e "g|export GTK_IM_MODULE=Maliit|d" /etc/xprofile
fi
}

