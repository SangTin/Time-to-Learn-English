package exception.editWord;

import data.Word;

public class ExistingWordException extends EditWordException {
    public ExistingWordException() {
        super("Word already exists");
    }

    public ExistingWordException(String message) {
        super(message);
    }

    public ExistingWordException(Word word) {
        super(String.format("Word \"%s\" already exists with id: %d", 
            word.getWordTarget(), word.getId()));
    }
}
