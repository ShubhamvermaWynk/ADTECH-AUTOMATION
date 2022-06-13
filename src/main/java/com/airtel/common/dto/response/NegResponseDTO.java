package com.airtel.common.dto.response;


public class NegResponseDTO {
	
	

		
		
		public String getErrorCode() {
			return errorCode;
		}
		public void setErrorCode(String errorCode) {
			this.errorCode = errorCode;
		}
		public String getErrorMessage() {
			return errorMessage;
		}
		public void setErrorMessage(String errorMessage) {
			this.errorMessage = errorMessage;
		}
		public String getHttpStatus() {
			return httpStatus;
		}
		public void setHttpStatus(String httpStatus) {
			this.httpStatus = httpStatus;
		}
		public String getDisplayMessage() {
			return displayMessage;
		}
		public void setDisplayMessage(String displayMessage) {
			this.displayMessage = displayMessage;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((displayMessage == null) ? 0 : displayMessage.hashCode());
			result = prime * result + ((errorCode == null) ? 0 : errorCode.hashCode());
			result = prime * result + ((errorMessage == null) ? 0 : errorMessage.hashCode());
			result = prime * result + ((httpStatus == null) ? 0 : httpStatus.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			NegResponseDTO other = (NegResponseDTO) obj;
			if (displayMessage == null) {
				if (other.displayMessage != null)
					return false;
			} else if (!displayMessage.equals(other.displayMessage))
				return false;
			if (errorCode == null) {
				if (other.errorCode != null)
					return false;
			} else if (!errorCode.equals(other.errorCode))
				return false;
			if (errorMessage == null) {
				if (other.errorMessage != null)
					return false;
			} else if (!errorMessage.equals(other.errorMessage))
				return false;
			if (httpStatus == null) {
				if (other.httpStatus != null)
					return false;
			} else if (!httpStatus.equals(other.httpStatus))
				return false;
			return true;
		}
		
		@Override
		public String toString() {
			return "NegResponseDTO [errorCode=" + errorCode + ", errorMessage=" + errorMessage + ", httpStatus="
					+ httpStatus + ", displayMessage=" + displayMessage + "]";
		}
		private String errorCode;
		private String errorMessage;
		private String httpStatus;
		private String displayMessage;
		
		
		
		

	}



