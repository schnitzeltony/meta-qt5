require python-pyqt5.inc

inherit pythonnative

DEPENDS += "python-sip python-sip-native python"

RDEPENDS_${PN}_append_class-target = " python-core python-sip"

