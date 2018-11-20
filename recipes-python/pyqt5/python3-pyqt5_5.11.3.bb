require python-pyqt5.inc

inherit python3native

DEPENDS += "python3-sip python3-sip-native python3"

RDEPENDS_${PN}_append_class-target = " python3-core python3-sip"
