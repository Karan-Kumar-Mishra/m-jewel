package customcomponents;

import java.awt.Point;
import java.util.List;
import java.util.function.Function;
import javax.swing.text.JTextComponent;

public class TextComponentSuggestionClient implements SuggestionClient<JTextComponent> {

  private final Function<String, List<String>> suggestionProvider;

  public TextComponentSuggestionClient(Function<String, List<String>> suggestionProvider) {
      this.suggestionProvider = suggestionProvider;
  }

  @Override
  public Point getPopupLocation(JTextComponent invoker) {
      return new Point(0, invoker.getPreferredSize().height);
  }

  @Override
  public void setSelectedText(JTextComponent invoker, String selectedValue) {
      invoker.setText(selectedValue);
  }

  @Override
  public List<String> getSuggestions(JTextComponent invoker) {
      return suggestionProvider.apply(invoker.getText().trim());
  }
}
