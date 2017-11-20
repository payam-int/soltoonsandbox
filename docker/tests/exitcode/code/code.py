import os, sys

if 'EXITCODE' in os.environ:
    sys.exit(int(os.environ['EXITCODE']))
else:
    sys.exit(-1)
