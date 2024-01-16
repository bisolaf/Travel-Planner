# stencil_setup.py
# When you start the assignment, run this file to make sure your
# stencil environment is configured correctly.  

import nltk
from nltk.corpus import stopwords
from tqdm import tqdm

# Install stopwords with SSL verification disabled
# This should work even if Python's extra certificates are not installed
import ssl
try:
    _create_unverified_https_context = ssl._create_unverified_context
except AttributeError:
    pass
else:
    ssl._create_default_https_context = _create_unverified_https_context
nltk.download('stopwords')
print("Setup complete!")