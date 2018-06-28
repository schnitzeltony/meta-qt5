inherit qmake5

require qwt-qt5.inc

SRC_URI[qwt.md5sum] = "9c88db1774fa7e3045af063bbde44d7d"
SRC_URI[qwt.sha256sum] = "2b08f18d1d3970e7c3c6096d850f17aea6b54459389731d3ce715d193e243d0c"

RPROVIDES_${PN}-dev = "libqwt-dev"
