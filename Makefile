TARGET         = quickir.jar
LANG           = java
RES            = ma
LIBD5MANEXPORT = /data/main/man/program/mdvl-d5man-1.0.0/libd5manexport
XFLAGS         = -Xlint:deprecation
MAIN           = ma.quickir.Main
TEST_DEF       = program

# aka. if exists
ifneq ("$(wildcard $(LIBD5MANEXPORT))","")
XCP = $(LIBD5MANEXPORT)
else
XCP = minitools
endif

XRES = $(XCP)/ma

include /usr/share/mdvl/make.mk

test: all
	$(Q)find /usr/share/common-licenses -type f | \
						java $(CPV) $(MAIN) TEXT $(TEST)

test-large: all
	$(Q)find /usr/share/common-licenses -type f | \
			java $(CPV) $(MAIN) \
			TEXT "$$(cat /usr/share/common-licenses/Apache-2.0)"
