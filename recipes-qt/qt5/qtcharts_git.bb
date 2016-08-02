require qt5.inc
require qt5-git.inc

QT_MODULE_BRANCH = "5.7.0"

LICENSE = "GPL-3.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE.GPL3;md5=d32239bcb673463ab874e80d47fae504 \
"

DEPENDS += "qtbase qtdeclarative qtmultimedia"

SRCREV = "8a781841812a42ed728a76c0dcdcaa6196eac44e"
