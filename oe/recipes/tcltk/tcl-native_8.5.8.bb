inherit native
include tcl_8.5.8.bb

#MobiAqua: native version without fixed-system.patch
SRC_URI = "\
  ${SOURCEFORGE_MIRROR}/tcl/tcl${PV}-src.tar.gz \
  file://confsearch.diff;striplevel=2 \
  file://manpages.diff;striplevel=2 \
  file://non-linux.diff;striplevel=2 \
  file://rpath.diff;striplevel=2 \
  file://tcllibrary.diff;striplevel=2 \
  file://tclpackagepath.diff;striplevel=2 \
  file://tclprivate.diff;striplevel=2 \
  file://mips-tclstrtod.patch;patchdir=..;striplevel=0 \
"
