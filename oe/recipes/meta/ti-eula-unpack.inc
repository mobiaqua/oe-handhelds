# This file defines function used for unpacking the .bin file downloaded over
# the http and display EULA.
#  BINFILE - name of the install jammer .bin file

python do_unpack () {
    bb.build.exec_func('base_do_unpack', d)
    bb.build.exec_func('ti_bin_do_unpack', d)
}

TI_BIN_UNPK_WDEXT += ""
python ti_bin_do_unpack() {

    import os

    localdata = bb.data.createCopy(d)
    bb.data.update_data(localdata)

    binfile  = bb.data.getVar('BINFILE', localdata)
    binfile  = bb.data.expand(binfile, localdata)

    # Change to the working directory
    save_cwd = os.getcwd()
    workdir  = bb.data.getVar('WORKDIR', localdata)
    workdir  = bb.data.expand(workdir, localdata)
    os.chdir(workdir)

    # Make the InstallJammer binary executable so we can run it
    os.chmod(binfile, 0755)

    # Run the InstallJammer binary and accept the EULA
    filename = "HOME=%s ./%s --mode unattended" % (workdir, binfile)

    if os.system(filename) != 0:
        print "ERROR: ti-eula-unpack: failed to execute binary installer"
        raise bb.build.FuncFailed()

    # Return to the previous directory
    os.chdir(save_cwd)
}

