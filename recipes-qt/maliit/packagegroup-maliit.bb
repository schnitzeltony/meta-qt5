SUMMARY = "Meta package for a working maliit"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=4d92cd373abda3937c2bc47fbc49d690"

inherit packagegroup

RDEPENDS_${PN} = " \
    maliit-framework-qt5 \
    maliit-inputcontext-qt5 \
    maliit-inputcontext-gtk-qt5 \
    maliit-plugins-qt5 \
"
