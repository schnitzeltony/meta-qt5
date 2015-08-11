DESCRIPTION = "A virtual keyboard for touch-screen based user interfaces"

LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://LICENSE.LGPL;md5=5c917f6ce94ceb8d8d5e16e2fca5b9ad"

inherit qmake5

DEPENDS = "qtdeclarative"

SRC_URI = "git://github.com/maliit/framework.git \
    file://0001-Fix-MALIIT_INSTALL_PRF-to-allow-the-build-with-opene.patch \
    file://maliit-server.desktop \
"

SRCREV = "60b1b10de14f932420313c547ab801daf522d539"
PV = "0.99.0+git${SRCPV}"
S = "${WORKDIR}/git"

EXTRA_QMAKEVARS_PRE += "\
    CONFIG+=nodoc \
    CONFIG+=noxcb \
    CONFIG+=notests \
    CONFIG+=enable-dbus-activation \
    CONFIG+=qt5-inputcontext \
    MALIIT_INSTALL_PRF=${QMAKE_MKSPEC_PATH}/mkspecs/features \
"

do_install_append() {
    # Fix absolute paths
    sed -i -e "s|/usr|${STAGING_DIR_TARGET}${prefix}|" ${D}/${libdir}/${QT_DIR_NAME}/mkspecs/features/maliit-framework.prf
    sed -i -e "s|/usr|${STAGING_DIR_TARGET}${prefix}|" ${D}/${libdir}/${QT_DIR_NAME}/mkspecs/features/maliit-plugins.prf
    install -d ${D}${datadir}/applications
    install -m 644 ${WORKDIR}/maliit-server.desktop ${D}${datadir}/applications
}

PACKAGES += "maliit-inputcontext-qt5 ${PN}-examples"

FILES_${PN} += "\
    ${libdir}/*.so* \
    ${bindir} \
    ${datadir}/applications/maliit-server.desktop \
    ${datadir}/dbus-1 \
"

FILES_maliit-inputcontext-qt5 = "\
    ${OE_QMAKE_PATH_PLUGINS}/platforminputcontexts \
"

FILES_${PN}-examples = "\
    ${libdir}/maliit-framework-tests/plugins/examples \
"

FILES_${PN}-dev += "\
    ${includedir}/maliit \
    ${libdir}/pkgconfig \
    ${libdir}/qt5/mkspecs \
"

FILES_${PN}-dbg += "\
    ${OE_QMAKE_PATH_PLUGINS}/platforminputcontexts/.debug \
    ${libdir}/maliit-framework-tests/plugins/examples/*/*/.debug \
"

pkg_postinst_${PN} () {
#!/bin/sh
# should run online
if [ "x$D" != "x" ]; then
    exit 1
fi
echo "export QT_IM_MODULE=Maliit" >> /etc/xprofile
ln -s /usr/share/applications/maliit-server.desktop /etc/xdg/autostart/maliit-server.desktop
}

pkg_postrm_${PN} () {
#!/bin/sh
# should run online
if [ "x$D" = "x" ]; then
    exit 1
fi
if [ -e "/etc/xprofile" ]; then
    sed -i -e "g|export QT_IM_MODULE=Maliit|d" /etc/xprofile
fi
rm -f /etc/xdg/autostart/maliit-server.desktop
}
